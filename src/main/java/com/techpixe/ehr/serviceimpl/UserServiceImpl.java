package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.*;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<Map<String, Object>> allUser() {
        List<HR> fetchAllUsers = userRepository.findAll();
        List<Map<String, Object>> response= new ArrayList<>();

        for (HR hr : fetchAllUsers) {
            Map<String, Object> employeeData = new HashMap<>();

            employeeData.put("user_Id",hr.getUser_Id());
            employeeData.put("companyName",hr.getCompanyName());
            employeeData.put("authorizedCompanyName",hr.getAuthorizedCompanyName());
            employeeData.put("fullName",hr.getClients().getFullName());
            employeeData.put("email",hr.getClients().getEmail());
            employeeData.put("mobileNumber",hr.getClients().getMobileNumber());
            employeeData.put("address",hr.getAddress());
            response.add(employeeData);

        }
        return response;
    }

    @Override
    public List<EmployeeTable> getEmployeesByUserId(Long userId) {
        HR user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getEmployeeTables();
        return user.getEmployeeTables();
    }

    @Override
    public List<Map<String, Object>> getEmployeesByUser(Long userId) {
        HR user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<EmployeeTable> data = user.getEmployeeTables();
        List<Map<String, Object>> response = new ArrayList<>();
        for (EmployeeTable employeeDetails : data) {
            Map<String, Object> employeeData = new HashMap<>();
            employeeData.put("id", employeeDetails.getId());

            if (employeeDetails.getClients() != null) {
                employeeData.put("fullName", employeeDetails.getClients().getFullName());
                employeeData.put("emailId", employeeDetails.getClients().getEmail());
                employeeData.put("contactNo", employeeDetails.getClients().getMobileNumber());

            }
            employeeData.put("empCode", employeeDetails.getEmpCode());
            employeeData.put("dob", employeeDetails.getDob());
            employeeData.put("gender", employeeDetails.getGender());
            employeeData.put("maritalStatus", employeeDetails.getMaritalStatus());
            employeeData.put("nationality", employeeDetails.getNationality());
            employeeData.put("address", employeeDetails.getAddress());
            employeeData.put("city", employeeDetails.getCity());
            employeeData.put("state", employeeDetails.getState());
            employeeData.put("country", employeeDetails.getCountry());
            employeeData.put("identification", employeeDetails.getIdentification());
            employeeData.put("idNumber", employeeDetails.getIdNumber());
            employeeData.put("employeeType", employeeDetails.getEmployeeType());
            employeeData.put("joiningDate", employeeDetails.getJoiningDate());
            employeeData.put("bloodGroup", employeeDetails.getBloodGroup());
            employeeData.put("designation", employeeDetails.getDesignation());
            employeeData.put("department", employeeDetails.getDepartment());
            employeeData.put("panNo", employeeDetails.getPanNo());
            employeeData.put("bankName", employeeDetails.getBankName());
            employeeData.put("bankAccountNo", employeeDetails.getBankAccountNo());
            employeeData.put("iFSCCode", employeeDetails.getIFSCCode());
            employeeData.put("pfAccountNo", employeeDetails.getPfAccountNo());
            response.add(employeeData);


        }
        return response;
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
