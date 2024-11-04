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
    SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("$(spring.mail.username)")
    private String fromMail;

    public static String generatePassword() {
        Random random = new Random();

        StringBuilder lettersBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(LETTERS.length());
            lettersBuilder.append(LETTERS.charAt(index));
        }

        StringBuilder digitsBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(DIGITS.length());
            digitsBuilder.append(DIGITS.charAt(index));
        }

        return lettersBuilder.toString() + digitsBuilder;
    }

    @Override
    public HR registration(String email, Long mobileNumber, String fullName, String planType,
                           String companyName, String authorizedCompanyName, MultipartFile logo, String address) throws IOException {

       // SubscriptionPlan subscriptionId = subscriptionPlanRepository.findById(subscriptionPlan.orElseThrow(() -> new RuntimeException(subscriptionPlan + "is not present"));
        HR user = new HR();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobileNumber(mobileNumber);
        //user.setSubscriptionPlan(subscriptionId);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = generatePassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setAuthorizedCompanyName(authorizedCompanyName);
        user.setCompanyName(companyName);
        user.setAddress(address);
        user.setLogo(logo.getBytes());
        user.setActive(true);
        this.sendmail(fullName, email, mobileNumber, password);

        return userRepository.save(user);

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
    public ResponseEntity<?> changePassword(Long user_Id, String password, String confirmPassword) {
        Object[] user = userRepository.findPartialUserById(user_Id);
        // System.out.println(Arrays.deepToString(user));

        HR userEntity = new HR();

        Object[] userDetails = (Object[]) user[0];
        String userPassword = (String) userDetails[3];
        // System.out.println(userPassword);
        if (user != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if (user != null && bCryptPasswordEncoder.matches(password, userPassword)) {
                userEntity.setPassword(bCryptPasswordEncoder.encode(confirmPassword));
                userRepository.save(userEntity);
                return ResponseEntity.ok("Password Changed Successfully");

            } else {
                ErrorResponseDto error = new ErrorResponseDto();
                error.setError("please enter the correct password:" + password);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        } else {
            ErrorResponseDto error = new ErrorResponseDto();
            error.setError("User is not present:" + user_Id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        }

    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        HR user = userRepository.findByEmail(email);
        HR saveUser = new HR();
        if (user != null) {
            // System.out.println(user);
            // Object[] userDetails = (Object[]) user[0];

            String password = generatePassword();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            saveUser.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(saveUser);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromMail);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("password changed Successfully in E_HR application\n");
            simpleMailMessage.setText("Dear " + user.getFullName()
                    + "\n\nPlease check your  email and generated password\n UserEmail  :" + email
                    + "\n  MobileNumber :" + user.getMobileNumber() + "\n New Password   :" + password + "\n\n"
                    + "you will be required to reset the New password upon login\n\n\n if you have any question or if you would like to request a call-back,please email us at support info@techpixe.com");
            javaMailSender.send(simpleMailMessage);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("forgot password successfully");
        } else {

            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            errorResponseDto.setError("Invalid Email");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
        }
    }

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
