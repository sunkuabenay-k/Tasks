package com.app.eventService.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "event_notifications";

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventNotification(String message) {
        kafkaTemplate.send(TOPIC, message);
        
    }
}
