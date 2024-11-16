package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<HR> getByUserId(Long user_Id);

    List<Map<String, Object>> allUser();

    List<EmployeeTable> getEmployeesByUserId(Long userId);
    List<Map<String, Object>> getEmployeesByUser(Long userId);

    List<AddJobDetails> getAddJobDetailsByUserId(Long userId);

    List<EmployeeTable> getEmployeeTableByUserId(Long userId);

    List<Holiday> getHolidayByUserId(Long userId);

    List<PayHeads> getPayHeadsByUserId(Long userId);

    List<PersonalInformation> getPersonalInformationByUserId(Long userId);

    List<LeaveApprovalTable> getLeaveApprovalsByUserId(Long userId);

    List<Attendance> getAttendanceByUserId(Long userId);

}
