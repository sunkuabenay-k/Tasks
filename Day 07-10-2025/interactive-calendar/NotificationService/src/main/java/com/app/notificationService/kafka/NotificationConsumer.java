package com.app.notificationService.kafka;

import com.app.notificationService.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "event_notifications", groupId = "notification_group")
    public void consumeNotification(String message) {
        System.out.println("Received notification message: " + message);
        notificationService.saveNotification(message);
    }
}
