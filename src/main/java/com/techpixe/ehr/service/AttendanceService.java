package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.Attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    Attendance createAttendance(Long empId, LocalTime punchIn, String punchInMessage, LocalDate date);



    Attendance updateAttendance(Long id,LocalTime punchOut,
                                String punchOutMessage,
                                LocalTime punchIn,
                                String  punchInMessage,
                                LocalDate  date,
                                String   name);


    Attendance getAttendanceByDate(Long empId, LocalDate date);

}
