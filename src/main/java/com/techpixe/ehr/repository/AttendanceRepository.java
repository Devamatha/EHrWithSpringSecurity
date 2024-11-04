package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByEmployeeTableIdAndDate(Long empId, LocalDate date);

}
