package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    Attendance createAttendance(Attendance attendance);

    List<Attendance> getAllAttendances();

    Optional<Attendance> getAttendanceById(Long id);

    Attendance updateAttendance(Long id, Attendance attendanceDetails);

    void deleteAttendance(Long id);

    Attendance getAttendanceByDate(Long empId, LocalDate date);

}
