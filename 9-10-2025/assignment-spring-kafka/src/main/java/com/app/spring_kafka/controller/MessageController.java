package com.app.spring_kafka.controller;

import com.app.spring_kafka.kafka.KafkaProducer;
import com.app.spring_kafka.model.MessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final KafkaProducer kafkaProducer;

    public MessageController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping
    public ResponseEntity<String> send(@RequestBody MessageRequest request) {
        kafkaProducer.sendMessage(request.getKey(), request.getMessage());
        return ResponseEntity.ok("Message sent to Kafka");
    }
}
