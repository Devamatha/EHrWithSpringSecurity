package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.entity.Notification;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.repository.NotificationRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeTableRepository employeeRepository;

    @Override
    public void sendNotificationToEmployee(Long senderId, Long receiverId, String messageContent, String messageType) {
        HR sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        EmployeeTable receiver = employeeRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setRecipient(receiver);
        notification.setMessageContent(messageContent);
        notification.setTimestamp(LocalDateTime.now());
        notification.setReadStatus(false);
        notification.setMessageType(messageType);
        notificationRepository.save(notification);
    }

    @Override
    public void sendNotificationToEmployees(Long senderId, List<Long> receiverIds, String messageContent, String messageType) {
        HR sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));


        for (Long receiverId : receiverIds) {

            EmployeeTable receiver = employeeRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
            Notification notification = new Notification();
            notification.setSender(sender);
            notification.setRecipient(receiver);
            notification.setMessageContent(messageContent);
            notification.setTimestamp(LocalDateTime.now());
            notification.setReadStatus(false);

            notificationRepository.save(notification);

        }
    }

    @Override
    public List<Notification> getNotificationsForEmployee(Long employeeId) {
        EmployeeTable employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return notificationRepository.findAllByRecipient(employee);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
}
