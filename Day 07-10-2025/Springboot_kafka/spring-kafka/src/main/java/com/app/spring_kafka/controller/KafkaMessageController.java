package com.app.spring_kafka.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.app.spring_kafka.service.KafkaProducerService;

@RestController
@RequestMapping("/api/kafka")
public class KafkaMessageController {

    @Autowired
    private KafkaProducerService producerService;

    @PostMapping("/publish")
    public String publish(@RequestParam String message) {
        producerService.sendMessage(message);
        return "Message published to Kafka: " + message;
    }
}

