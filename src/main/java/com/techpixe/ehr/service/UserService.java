package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    HR registration(String email, Long mobileNumber, String fullName, String planType,
                    String companyName, String authorizedCompanyName, MultipartFile logo, String address) throws IOException;

    ResponseEntity<?> changePassword(Long user_Id, String password, String confirmPassword);

    ResponseEntity<?> forgotPassword(String email);

    Optional<HR> getByUserId(Long user_Id);

    List<HR> allUser();

    List<EmployeeTable> getEmployeesByUserId(Long userId);

    List<AddJobDetails> getAddJobDetailsByUserId(Long userId);

    List<EmployeeTable> getEmployeeTableByUserId(Long userId);

    List<Holiday> getHolidayByUserId(Long userId);

    List<PayHeads> getPayHeadsByUserId(Long userId);

    List<PersonalInformation> getPersonalInformationByUserId(Long userId);

    List<LeaveApprovalTable> getLeaveApprovalsByUserId(Long userId);

    List<Attendance> getAttendanceByUserId(Long userId);

}
