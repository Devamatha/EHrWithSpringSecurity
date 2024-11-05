package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.dto.ErrorResponseDto;
import com.techpixe.ehr.entity.*;
import com.techpixe.ehr.repository.SubscriptionPlanRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("$(spring.mail.username)")
    private String fromMail;

    public static String generatePassword() {
        Random random = new Random();

        StringBuilder lettersBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(LETTERS.length());
            lettersBuilder.append(LETTERS.charAt(index));
        }

        StringBuilder digitsBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(DIGITS.length());
            digitsBuilder.append(DIGITS.charAt(index));
        }

        return lettersBuilder.toString() + digitsBuilder;
    }



    public void sendmail(String fullName, String email, Long mobileNumber, String password) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Registration completed Successfully in User\n");
        simpleMailMessage.setText("Dear " + fullName
                + ",\n\n Thank you for singing Up for E_HR! click below to get  started on your web or mobile device .\n\nPlease check your registered email and generted passowrd\n UserEmail  :"
                + email + "\n Registered MobileNumber :" + mobileNumber + "\n Temporary Password   :" + password
                + "\n\n"
                + "you will be required to reset the temporary password upon login\n\n\n if you have any question or if you would like to request a call-back,please email us at support info@techpixe.com");
        javaMailSender.send(simpleMailMessage);
    }

    // ***************CHANGE PASSWORD*************************



    @Override
    public Optional<HR> getByUserId(Long user_Id) {
        return userRepository.findById(user_Id);
    }

    @Override
    public List<HR> allUser() {
        List<HR> fetchAllUsers = userRepository.findAll();
        if (fetchAllUsers == null) {
            throw new ResponseStatusException(HttpStatus.OK, "No Users Found");
        }
        return fetchAllUsers;
    }

    @Override
    public List<EmployeeTable> getEmployeesByUserId(Long userId) {
        HR user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getEmployeeTables();
    }

    @Override
    public List<AddJobDetails> getAddJobDetailsByUserId(Long userId) {
        return userRepository.findAddJobDetailsByUserId(userId);
    }

    @Override
    public List<EmployeeTable> getEmployeeTableByUserId(Long userId) {
        return userRepository.findEmployeeTableByUserId(userId);
    }

    @Override
    public List<Holiday> getHolidayByUserId(Long userId) {
        return userRepository.findHolidayByUserId(userId);
    }

    @Override
    public List<PayHeads> getPayHeadsByUserId(Long userId) {
        return userRepository.findPayHeadsByUserId(userId);
    }

    @Override
    public List<PersonalInformation> getPersonalInformationByUserId(Long userId) {
        return userRepository.findPersonalInformationByUserId(userId);
    }

    @Override
    public List<LeaveApprovalTable> getLeaveApprovalsByUserId(Long userId) {
        return userRepository.findLeaveApprovalsByUserId(userId);
    }

    @Override
    public List<Attendance> getAttendanceByUserId(Long userId) {
        return userRepository.findAttendanceByUserId(userId);
    }


}
