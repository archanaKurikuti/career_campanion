package com.example.career_companion.service;

import com.example.career_companion.dto.notification.NotificationResponse;
import com.example.career_companion.entity.Notification;
import com.example.career_companion.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<NotificationResponse> getUserNotifications(
            Long userId) {

        return notificationRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public NotificationResponse markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"));

        notification.setRead(true);

        Notification saved =
                notificationRepository.save(notification);

        return mapToResponse(saved);
    }

    public void deleteNotification(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"));

        notificationRepository.delete(notification);
    }

    private NotificationResponse mapToResponse(
            Notification notification) {

        NotificationResponse response =
                new NotificationResponse();

        response.setId(notification.getId());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType());
        response.setRead(notification.isRead());
        response.setCreatedAt(notification.getCreatedAt());

        return response;
    }
}