package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.dto.ErrorResponseDto;
import com.techpixe.ehr.dto.LoginDto;
import com.techpixe.ehr.entity.Admin;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.repository.AdminRepository;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserRepository userRepository;

//	
//	@Autowired
//	private JavaMailSender javaMailSender;
//
//	@Value("$(spring.mail.username)")
//	private String fromMail;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EmployeeTableRepository employeeTableRepository;

    @Override
    public Admin registerAdmin(String fullName, String email, Long mobileNumber, String password) {

        Admin admin = new Admin();
        admin.setFullName(fullName);
        admin.setEmail(email);
        admin.setMobileNumber(mobileNumber);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        admin.setPassword(bCryptPasswordEncoder.encode(password));
        admin.setRole("admin");
        return adminRepository.save(admin);
    }

    @Override
    public ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password) {
        Admin user = adminRepository.findByMobileNumber(mobileNumber);
        HR user1 = userRepository.findByMobileNumber(mobileNumber);
        EmployeeTable employeeDetails = employeeTableRepository.findByContactNo(mobileNumber);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        LoginDto loginDto = new LoginDto();

        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            loginDto.setId(user.getAdmin_Id());
            loginDto.setFullName(user.getFullName());
            loginDto.setEmail(user.getEmail());
            loginDto.setMobileNumber(user.getMobileNumber());
            loginDto.setRole(user.getRole());
            return ResponseEntity.ok(loginDto);
        } else if (user1 != null && bCryptPasswordEncoder.matches(password, user1.getPassword())) {
            loginDto.setId(user1.getUser_Id());
            loginDto.setFullName(user1.getFullName());
            loginDto.setEmail(user1.getEmail());
            loginDto.setMobileNumber(user1.getMobileNumber());
            loginDto.setRole("USER");
            // loginDto.setSubcriptionPlan(user1.getSubscriptionPlan().getSubscription_Id());

            return ResponseEntity.ok(loginDto);
        } else if (employeeDetails != null && bCryptPasswordEncoder.matches(password, employeeDetails.getPassword())) {
            loginDto.setFullName(employeeDetails.getFullName());
            loginDto.setEmail(employeeDetails.getEmailId());
            loginDto.setMobileNumber(employeeDetails.getContactNo());
            loginDto.setRole(employeeDetails.getRole());
            loginDto.setId(employeeDetails.getId());
            return ResponseEntity.ok(loginDto);

        } else {

            ErrorResponseDto errorResponse = new ErrorResponseDto();
            errorResponse.setError("Invalid mobile number or password");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<?> loginByEmail(String email, String password) {
        Admin user = adminRepository.findByEmail(email);
        HR user1 = userRepository.findByEmail(email);
        EmployeeTable employeeDetails = employeeTableRepository.findByEmailId(email);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        LoginDto loginDto = new LoginDto();

        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            loginDto.setId(user.getAdmin_Id());
            loginDto.setFullName(user.getFullName());
            loginDto.setEmail(user.getEmail());
            loginDto.setMobileNumber(user.getMobileNumber());
            loginDto.setRole(user.getRole());

            return ResponseEntity.ok(loginDto);

        } else if (user1 != null && bCryptPasswordEncoder.matches(password, user1.getPassword())) {


            loginDto.setId(user1.getUser_Id());
            loginDto.setFullName(user1.getFullName());
            loginDto.setEmail(user1.getEmail());
            loginDto.setMobileNumber(user1.getMobileNumber());
            loginDto.setRole("USER");


            // photoGrapherDTo.setSubcriptionPlan(user1.getSubscriptionPlan().getSubscription_Id());

            return ResponseEntity.ok(loginDto);
        } else if (employeeDetails != null && bCryptPasswordEncoder.matches(password, employeeDetails.getPassword())) {
            loginDto.setFullName(employeeDetails.getFullName());

            loginDto.setEmail(employeeDetails.getEmailId());
            loginDto.setMobileNumber(employeeDetails.getContactNo());
            loginDto.setRole(employeeDetails.getRole());

            loginDto.setId(employeeDetails.getId());
            return ResponseEntity.ok(loginDto);

        } else {

            ErrorResponseDto errorResponse = new ErrorResponseDto();
            errorResponse.setError("Invalid password");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    //
//	// ***************CHANGE PASSWORD*************************
//
//	@Override
//	public ResponseEntity<?> changePassword(Long admin_Id, String password, String confirmPassword) {
//		Admin user = adminRepository.findById(admin_Id).orElse(null);
//		PhotoGrapher user1 = PhotoGrapherRepository.findById(admin_Id)
//				.orElseThrow(() -> new RuntimeException("photographer is not present: " + admin_Id));
//		if (user != null && user.getPassword().equals(password)) {
//
//			logger.debug("Admin Password is Successfully Changed");
//			logger.info("Request comes from Admin Controller to Admin ServiceImpl through Service");
//
//			AdminDto adminDto = new AdminDto();
//			adminDto.setAdmin_Id(user.getAdmin_Id());
//			adminDto.setFullName(user.getFullName());
//			adminDto.setEmail(user.getEmail());
//			adminDto.setMobileNumber(user.getMobileNumber());
//			adminDto.setRole(user.getRole());
//			adminDto.setPassword(confirmPassword);
//
//			user.setPassword(confirmPassword);
//			adminRepository.save(user);
//			return ResponseEntity.ok(adminDto);
//
//		} else if (user1 != null && user1.getPassword().equals(password)) {
//
//			logger.debug("PhotoGrapher Password is Successfully Changed");
//			logger.info("Request comes from Admin Controller to Admin ServiceImpl through Service");
//
//			PhotoGrapherDTo photoGrapherDTo = new PhotoGrapherDTo();
//			photoGrapherDTo.setPhotographer_Id(user1.getPhotographer_Id());
//			photoGrapherDTo.setFullName(user1.getFullName());
//			photoGrapherDTo.setEmail(user1.getEmail());
//			photoGrapherDTo.setMobileNumber(user1.getMobileNumber());
//			photoGrapherDTo.setPassword(confirmPassword);
//			photoGrapherDTo.setRole(user1.getRole());
//
//			user1.setPassword(confirmPassword);
//			PhotoGrapherRepository.save(user1);
//			return ResponseEntity.ok(photoGrapherDTo);
//		} else {
//
//			logger.error("Password and Confirm Password are not Matching");
//
//			ErrorResponseDto error = new ErrorResponseDto();
//			error.setError("######################Password is not present#################");
//			// return ResponseEntity.internalServerError().body(error);
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//		}
//	}
//
//	// *************FORGOT PASSWORD*****************
//
//	@Override
//	public ResponseEntity<?> forgotPassword(String email) {
//		Admin admin = adminRepository.findByEmail(email);
//		PhotoGrapher photoGrapher = PhotoGrapherRepository.findByEmail(email);
//
//		if (admin != null) {
//
//			logger.debug("Admin : Password will be sent to E-Mail Number");
//			logger.info("Request comes from Admin Controller to Admin ServiceImpl through Service");
//
//			AdminDto adminDTO = new AdminDto();
//			adminDTO.setAdmin_Id(admin.getAdmin_Id());
//			adminDTO.setFullName(admin.getFullName());
//			adminDTO.setEmail(email);
//			adminDTO.setMobileNumber(admin.getMobileNumber());
//			adminDTO.setRole(admin.getRole());
//
//			String password = generatePassword();
//			adminDTO.setPassword(password);
//
//			admin.setPassword(password);
//			adminRepository.save(admin);
//
//			return ResponseEntity.ok(adminDTO);
//		} else if (photoGrapher != null) {
//
//			logger.debug("PhotoGrapher : Password will be sent to E-Mail Number");
//			logger.info("Request comes from Admin Controller to Admin ServiceImpl through Service");
//
//			PhotoGrapherDTo photoGrapherDTO = new PhotoGrapherDTo();
//			photoGrapherDTO.setPhotographer_Id(photoGrapher.getPhotographer_Id());
//			photoGrapherDTO.setFullName(photoGrapher.getFullName());
//			photoGrapherDTO.setEmail(email);
//			photoGrapherDTO.setMobileNumber(photoGrapher.getMobileNumber());
//			photoGrapherDTO.setRole(photoGrapher.getRole());
//
//			String password = generatePassword();
//			photoGrapherDTO.setPassword(password);
//			photoGrapher.setPassword(password);
//			PhotoGrapherRepository.save(photoGrapher);
//
//			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//			simpleMailMessage.setFrom(fromMail);
//			simpleMailMessage.setTo(email);
//			simpleMailMessage.setSubject("Registration completed Successfully in GetPhoto application\n");
//			simpleMailMessage.setText("Dear " + photoGrapher.getFullName()
//					+ "\n\nPlease check your  email and generted passowrd\n UserEmail  :" + email + "\n  MobileNumber :"
//					+ photoGrapher.getMobileNumber() + "\n New Password   :" + password + "\n\n"
//					+ "you will be required to reset the New password upon login\n\n\n if you have any question or if you would like to request a call-back,please email us at support info@techpixe.com");
//			javaMailSender.send(simpleMailMessage);
//
//			return ResponseEntity.ok(photoGrapherDTO);
//		} else {
//
//			logger.error("****Invalid Email****");
//
//			ErrorResponseDto errorResponseDto = new ErrorResponseDto();
//			errorResponseDto.setError("Email is not matching");
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
//		}
//	}
//
//	// **********Generate Random Password ********************
//	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//	private static final String NUMERIC_STRING = "0123456789";
//
//	public static String generatePassword() {
//		StringBuilder builder = new StringBuilder();
//		Random random = new Random();
//
//		for (int i = 0; i < 4; i++) {
//			int index = random.nextInt(ALPHA_NUMERIC_STRING.length());
//			builder.append(ALPHA_NUMERIC_STRING.charAt(index));
//		}
//		for (int i = 0; i < 4; i++) {
//			int index = random.nextInt(NUMERIC_STRING.length());
//			builder.append(NUMERIC_STRING.charAt(index));
//		}
//
//		return builder.toString();
//	}
//
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }
}
