package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find notifications by recipient, ordered by timestamp in descending order
    List<Notification> findByEmployeeTableOrderByTimestampDesc(EmployeeTable recipient);

    List<Notification> findAllByEmployeeTable(EmployeeTable employee);


}
