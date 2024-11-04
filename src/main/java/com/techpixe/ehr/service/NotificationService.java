package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.Notification;

import java.util.List;

public interface NotificationService {

    void sendNotificationToEmployee(Long senderId, Long receiverId, String messageContent, String messageType);


    List<Notification> getNotificationsForEmployee(Long employeeId);

    void markNotificationAsRead(Long notificationId);

    void sendNotificationToEmployees(Long userId, List<Long> receiverIds, String messageContent, String messageType);
}
