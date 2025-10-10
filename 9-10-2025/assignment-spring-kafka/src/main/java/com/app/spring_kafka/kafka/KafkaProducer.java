package com.app.spring_kafka.kafka;

import com.app.spring_kafka.config.KafkaConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String key, Object message) {
        kafkaTemplate.send(KafkaConfig.TOPIC_NAME, key, message);
        System.out.println("Produced message: " + message);
    }
}
