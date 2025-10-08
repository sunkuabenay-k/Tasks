package com.app.notificationService.service;

import com.app.notificationService.entity.Notification;
import com.app.notificationService.repository.NotificationRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification saveNotification(String message) {
        Notification notification = new Notification(message);
        return notificationRepository.save(notification);
    }

    // New method: Get all notifications
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // New method: Get notification by id
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    // New method: Delete notification by id
    public void deleteNotification(Long id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Notification not found with id " + id);
        }
    }

    // New method: Delete all notifications (optional cleanup)
    public void deleteAllNotifications() {
        notificationRepository.deleteAll();
    }
}
