package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    void sendNotificationToEmployee(Long senderId, Long receiverId, String messageContent, String messageType);


    List<Map<String, Object>> getNotificationsForEmployee(Long employeeId);

    void markNotificationAsRead(Long notificationId);

    void sendNotificationToEmployees(Long userId, List<Long> receiverIds, String messageContent, String messageType);
}
