# 🧾 Real-Time Premium Order Processing Pipeline – Detailed Documentation

## 📚 Table of Contents

| Section | Title                                      | Description                                                                 |
| -------- | ------------------------------------------ | --------------------------------------------------------------------------- |
| 1.       | Introduction                               | Overview of the system and its purpose                                      |
| 2.       | System Architecture Overview               | Key components and their interactions                                       |
| 3.       | Data Flow Process                          | Step-by-step event-driven workflow                                          |
| 4.       | DynamoDB Table Design                      | Schema and stream configuration details                                     |
| 5.       | EventBridge Pipes Configuration            | Filtering rules and routing logic                                           |
| 6.       | Lambda Functions Implementation            | Detailed explanation of the logic and code                                 |
| 7.       | Test Events and Validation                 | Example test inputs and expected outputs                                   |
| 8.       | Error Handling and Reliability             | DLQ setup, retry logic, and batch behavior                                 |
| 9.       | Common Issues and Fixes                    | Frequent configuration or runtime problems and solutions                   |
| 10.      | Architecture Diagram                       | Text-based system diagram for clarity                                      |
| 11.      | Cost and Scalability Considerations         | Cost optimization and scalability benefits                                 |
| 12.      | Summary                                    | Final overview of system design and capabilities                           |

---

## 1️⃣ Introduction

The **Real-Time Premium Order Processing Pipeline** is an event-driven architecture designed to handle **high-value e-commerce orders** in real time using **AWS serverless services**.

The main objectives are to:

- Efficiently process **only relevant orders**.
- Separate and manage **premium orders (> $1000)**.
- Ensure fault tolerance with **DLQ and retry mechanisms**.
- Reduce Lambda invocation costs through **filtering at the source**.

---

## 2️⃣ System Architecture Overview

### Components

| Component         | Description                                                                 |
| ----------------- | --------------------------------------------------------------------------- |
| **DynamoDB Table** | Stores all order information (`orderId`, `status`, `amount`, `email`, etc.) |
| **DynamoDB Streams** | Emits data change events (`INSERT`, `MODIFY`, `REMOVE`)                    |
| **EventBridge Pipes** | Filters stream events and routes them to the appropriate Lambda functions |
| **AWS Lambda Functions** | Executes business logic for both standard and premium orders         |
| **SQS DLQs** | Capture failed events for reprocessing or debugging                              |
| **CloudWatch Logs** | Track execution results and errors for each Lambda                        |

---

## 3️⃣ Data Flow Process

1. **New order event** is written into the `NewOrders` DynamoDB table.
2. **DynamoDB Stream** captures the change and emits an event.
3. **EventBridge Pipe 1** filters standard orders (status: `pending` or `shipped`) and routes them to the `OrderProcessorLambda`.
4. **EventBridge Pipe 2** filters `MODIFY` events where `status` changes from `pending → shipped` and forwards to `PremiumOrderLambda`.
5. **PremiumOrderLambda** applies a final check (`amount > 1000`) and performs premium actions (e.g., notifications).
6. **DLQ (SQS)** stores any failed events for later analysis.

---

## 4️⃣ DynamoDB Table Design

| Attribute       | Type   | Description                                |
| ---------------- | ------ | ------------------------------------------ |
| `orderId`        | String | Unique identifier for each order           |
| `status`         | String | Order status (`pending`, `shipped`, etc.)  |
| `amount`         | Number | Order value in USD                         |
| `customerEmail`  | String | Customer email address                     |
| `createdAt`      | String | Record creation timestamp                  |
| `updatedAt`      | String | Record last update timestamp               |

**Stream Configuration:**
- Enabled: ✅ `NEW_AND_OLD_IMAGES`
- Billing Mode: `PAY_PER_REQUEST`
- Table Name: `NewOrders`

---

## 5️⃣ EventBridge Pipes Configuration

### Pipe 1 – Standard Orders

**Filter Pattern**
```json
{
  "eventName": ["INSERT", "MODIFY"],
  "dynamodb": {
    "NewImage": {
      "status": { "S": ["pending", "shipped"] },
      "customerEmail": { "S": [{ "anything-but": "*test.com*" }] }
    }
  }
}
```
**Purpose:**
Filters non-test orders with valid statuses before routing to `OrderProcessorLambda`.

---

### Pipe 2 – Premium Orders

**Filter Pattern**
```json
{
  "eventName": ["MODIFY"],
  "dynamodb": {
    "NewImage": {
      "status": { "S": ["shipped"] },
      "customerEmail": { "S": [{ "anything-but": "*test.com*" }] }
    },
    "OldImage": {
      "status": { "S": ["pending"] }
    }
  }
}
```

**Purpose:**
Captures status transitions from `pending` to `shipped` for non-test customers.

**Target:** `PremiumOrderLambda`  
**DLQ:** `PremiumOrdersDLQ`  
**Batch Size:** 100  
**Invocation Type:** `REQUEST_RESPONSE`

---

## 6️⃣ Lambda Functions Implementation

### 6.1 `OrderProcessorLambda` – Standard Orders

```python
import json

def lambda_handler(event, context):
    for record in event.get("Records", []):
        new_image = record.get("dynamodb", {}).get("NewImage", {})
        order_id = new_image.get("orderId", {}).get("S")
        amount = float(new_image.get("amount", {}).get("N", "0"))
        status = new_image.get("status", {}).get("S")

        if amount > 100:
            print(f"✅ Processed order {order_id} | Status: {status} | Amount: {amount}")
        else:
            print(f"⚠️ Skipped order {order_id} (Amount too low)")
    return {"statusCode": 200}
```

---

### 6.2 `PremiumOrderLambda` – High-Value Orders

```python
import json

def lambda_handler(event, context):
    premium_orders = []
    for record in event.get("Records", []):
        new = record["dynamodb"].get("NewImage", {})
        old = record["dynamodb"].get("OldImage", {})
        order_id = new.get("orderId", {}).get("S")
        new_status = new.get("status", {}).get("S")
        old_status = old.get("status", {}).get("S")
        amount = float(new.get("amount", {}).get("N", "0"))

        if old_status == "pending" and new_status == "shipped" and amount > 1000:
            print(f"🚀 Premium order shipped! Order ID: {order_id} | Amount: {amount}")
            premium_orders.append(order_id)
    return {"statusCode": 200, "premiumOrdersProcessed": premium_orders}
```

---

## 7️⃣ Test Events and Validation

**Sample Event**
```json
{
  "eventName": "MODIFY",
  "dynamodb": {
    "NewImage": {
      "orderId": { "S": "ORD-1001" },
      "status": { "S": "shipped" },
      "amount": { "N": "1500" },
      "customerEmail": { "S": "vip@example.com" }
    },
    "OldImage": {
      "status": { "S": "pending" }
    }
  }
}
```

**Expected Output**
```
🚀 Premium order shipped! Order ID: ORD-1001 | Amount=1500.0
```

---

## 8️⃣ Error Handling and Reliability

| Mechanism | Description |
| ---------- | ------------ |
| **DLQ (SQS)** | Captures unprocessed events for debugging. |
| **Retries** | Automatic retries for transient errors. |
| **Batch Processing** | Processes multiple events per invocation. |
| **Partial Failures** | Successful events continue even if some fail. |

---

## 9️⃣ Common Issues and Fixes

| Issue | Root Cause | Solution |
| ------ | ----------- | -------- |
| Irrelevant events processed | Incorrect filter pattern | Fixed event path references (`NewImage.status.S`). |
| No Lambda invocation | Filter too restrictive | Relaxed filter criteria to allow more valid events. |
| Type conversion error | Missing numeric attributes | Added safe parsing with defaults. |
| Test orders processed | Case sensitivity | Converted email to lowercase before comparison. |

---

## 🔟 Architecture Diagram (Text)

```
DynamoDB Table (NewOrders)
        │
        ▼
DynamoDB Stream
        │
        ▼
EventBridge Pipe 2 (status: pending→shipped)
        │
        ▼
PremiumOrderLambda (amount > 1000)
        │
        ▼
SQS Dead Letter Queue
```

---

## 11️⃣ Cost and Scalability Considerations

| Optimization | Description |
| ------------- | ------------ |
| **Event Filtering at Source** | Reduces Lambda invocations significantly. |
| **Batch Processing** | Minimizes cold starts. |
| **Serverless Architecture** | Scales automatically without manual intervention. |
| **DLQ Integration** | Prevents event loss. |
| **Pay-per-Request Billing** | Ensures cost efficiency for varying workloads. |

---

## 12️⃣ Summary

✅ Processes only relevant and high-value orders.  
✅ Uses **EventBridge Pipes** to minimize compute cost.  
✅ Employs **DLQs** for reliability and fault recovery.  
✅ Fully **serverless and scalable** design.  
✅ Ideal for production-level real-time order monitoring systems.
