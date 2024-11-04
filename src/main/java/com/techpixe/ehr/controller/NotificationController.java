package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.NotificationRequest;
import com.techpixe.ehr.entity.Notification;
import com.techpixe.ehr.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send/{userId}")
    public ResponseEntity<String> sendNotificationToEmployees(
            @PathVariable Long userId,
            @RequestBody NotificationRequest request) {
        try {
            notificationService.sendNotificationToEmployees(userId, request.getReceivers(), request.getMessageContent(), request.getMessageType());
            return ResponseEntity.ok("Notification sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification");
        }
    }

//    @PostMapping("/sendToAll/{senderId}")
//    public ResponseEntity<String> sendNotificationToAllEmployees(
//            @PathVariable Long senderId,
//            @RequestBody String messageContent) {
//        notificationService.sendNotificationToAllEmployees(senderId, messageContent);
//        return ResponseEntity.ok("Notifications sent to all employees successfully");
//    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Notification>> getNotificationsForEmployee(@PathVariable Long employeeId) {
        List<Notification> notifications = notificationService.getNotificationsForEmployee(employeeId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/markAsRead/{notificationId}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }


}
