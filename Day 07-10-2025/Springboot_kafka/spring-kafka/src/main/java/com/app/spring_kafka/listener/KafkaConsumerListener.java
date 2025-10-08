package com.app.spring_kafka.listener;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    @KafkaListener(topics = "test_topic", groupId = "spring-consumer")
    public void listen(String message) {
        System.out.println("Consumed: " + message);
    }
}
