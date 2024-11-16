package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.*;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    private boolean isEmail(String emailOrMobileNumber) {
        return emailOrMobileNumber.contains("@");
    }

    private boolean isMobileNumber(String emailOrMobileNumber) {
        return emailOrMobileNumber.matches("\\d+");
    }


    @GetMapping("/{id}")
    public ResponseEntity<HR> getJobDetailsById(@PathVariable("id") Long userId) {
        System.out.println(LocalDateTime.now() + "satrting");
        Optional<HR> jobDetails = userService.getByUserId(userId);
        System.out.println(LocalDateTime.now() + "ending");

        return jobDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/allUsers")
    public List<Map<String, Object>> allUsers() {
        List<Map<String, Object>> allUsers = userService.allUser();

        return allUsers;
    }


    @GetMapping("/{userId}/employees")
    public List<EmployeeTable> getEmployeesByUserId(@PathVariable Long userId) {
        return userService.getEmployeesByUserId(userId);
    }

    @GetMapping("/add-job-details/{userId}")
    public List<AddJobDetails> getAddJobDetails(@PathVariable Long userId) {
        return userService.getAddJobDetailsByUserId(userId);
    }

    @GetMapping("/employeedetails/{userId}")
    public List<Map<String, Object>> getEmployeeTableByUserId(@PathVariable Long userId) {
        return userService.getEmployeesByUser(userId);
    }

    @GetMapping("/holiday/{userId}")
    public List<Holiday> getHolidayByUserId(@PathVariable Long userId) {
        return userService.getHolidayByUserId(userId);
    }

    @GetMapping("/payHeads/{userId}")
    public List<PayHeads> getPayHeadByUserId(@PathVariable Long userId) {
        return userService.getPayHeadsByUserId(userId);
    }

    @GetMapping("/personalInformation/{userId}")
    public List<PersonalInformation> getPersonalInformationByUserId(@PathVariable Long userId) {
        return userService.getPersonalInformationByUserId(userId);
    }

    @GetMapping("/leave-approvals/{userId}")
    public List<LeaveApprovalTable> getLeaveApprovals(@PathVariable Long userId) {
        return userService.getLeaveApprovalsByUserId(userId);
    }

    @GetMapping("/attendance/{userId}")
    public List<Attendance> getAttedence(@PathVariable Long userId) {
        return userService.getAttendanceByUserId(userId);
    }

//    @RequestMapping("/user")
//    public HR getUserDetailsAfterLogin(Authentication authentication) {
//        HR optionalCustomer = userRepository.findByEmail(authentication.getName());
//        return optionalCustomer;
//    }


}
