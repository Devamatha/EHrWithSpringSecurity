package com.techpixe.ehr.service;

import com.techpixe.ehr.dto.EmployeePayHeadDTO;
import com.techpixe.ehr.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeTableService {
    EmployeeTable createEmployee(String fullName, Date dob, String gender, String maritalStatus, String nationality,
                                 String address, String city, String state, String country, String emailId, Long contactNo,
                                 String identification, String idNumber, String employeeType, LocalDate joiningDate, String bloodGroup,
                                 MultipartFile photograph, Long userId) throws IOException;

    List<EmployeeTable> getAllEmployees();

    Optional<EmployeeTable> getEmployeeById(Long id);

    Optional<EmployeeTable> updateEmployee(Long id, String empCode, Date dob, String gender,
                                           String maritalStatus, String nationality, String address, String city, String state, String country,
                                            String identification, String idNumber, String employeeType,
                                           LocalDate joiningDate, String bloodGroup,  String designation,
                                           String department, Long panNo, String bankName, Long bankAccountNo, String iFSCCode, String pfAccountNo,
                                           Integer totalDays, Integer presentDays, byte[] photograph, HR user);

    void deleteEmployee(Long id);

    List<AddPayHeadsToEmployee> getAddPayHeadsToEmployeeByEmployeeId(Long id);

    EmployeePayHeadDTO getEmployeeWithPayHeads(Long id);

    List<Attendance> getAttendanceByEmployee(Long id);

    List<LeaveApprovalTable> getLeaveapprovalTableByEmployeeId(Long id);

}
