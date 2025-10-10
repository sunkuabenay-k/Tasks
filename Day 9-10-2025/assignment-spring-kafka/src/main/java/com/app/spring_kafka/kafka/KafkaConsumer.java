package com.app.spring_kafka.kafka;

import com.app.spring_kafka.config.KafkaConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = KafkaConfig.TOPIC_NAME, groupId = "my-consumer-group")
    public void listen(Object message) {
        System.out.println("Consumed message: " + message);
    }
}
