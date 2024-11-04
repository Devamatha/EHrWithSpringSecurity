package com.techpixe.ehr.controller;

import com.techpixe.ehr.constant.ApplicationConstants;
import com.techpixe.ehr.dto.ErrorResponseDto;
import com.techpixe.ehr.dto.LoginRequestDTO;
import com.techpixe.ehr.dto.LoginResponseDTO;
import com.techpixe.ehr.entity.*;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    UserService userService;
@Autowired
    private UserRepository userRepository;

    private final Environment env;
    private final AuthenticationManager authenticationManager;
    @PostMapping("/register")
    public ResponseEntity<?> addRegistration(@RequestParam String email,
                                             @RequestParam Long mobileNumber, @RequestParam String fullName,
                                             @RequestParam(required = false) String planType, @RequestParam String companyName,
                                             @RequestParam String authorizedCompanyName, @RequestParam MultipartFile logo, @RequestParam String address)
            throws IOException {

        // System.err.println(" " + subscription_id + " " + email + " " + mobileNumber +
        // " " + fullName + " " + planType);

        HR registration = userService.registration(email, mobileNumber, fullName, planType,
                companyName, authorizedCompanyName, logo, address);

        // System.out.println("user registration success");

        return ResponseEntity.ok(registration);

    }

    private boolean isEmail(String emailOrMobileNumber) {
        return emailOrMobileNumber.contains("@");
    }

    private boolean isMobileNumber(String emailOrMobileNumber) {
        return emailOrMobileNumber.matches("\\d+");
    }

    @PostMapping("/changepassword/{user_Id}")
    public ResponseEntity<?> changePassword(@PathVariable Long user_Id, @RequestParam String password,
                                            @RequestParam String confirmPassword) {
        if (password != null && confirmPassword != null) {
            return userService.changePassword(user_Id, password, confirmPassword);
        } else {
            ErrorResponseDto error = new ErrorResponseDto();
            error.setError("*********Password is not present*************");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // *************FORGOT PASSWORD****************
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        if (email != null) {
            if (isEmail(email)) {
                return userService.forgotPassword(email);
            } else {
                ErrorResponseDto error = new ErrorResponseDto();
                error.setError("*********Invalid Email Pattern *************");
                return ResponseEntity.internalServerError().body(error);
            }
        } else {
            ErrorResponseDto error = new ErrorResponseDto();
            error.setError("*********Email is not present*************");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HR> getJobDetailsById(@PathVariable("id") Long userId) {
        System.out.println(LocalDateTime.now() + "satrting");
        Optional<HR> jobDetails = userService.getByUserId(userId);
        System.out.println(LocalDateTime.now() + "ending");

        return jobDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<HR>> allUsers() {
        List<HR> allUsers = userService.allUser();
        return new ResponseEntity<List<HR>>(allUsers, HttpStatus.OK);

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
    public List<EmployeeTable> getEmployeeTableByUserId(@PathVariable Long userId) {
        return userService.getEmployeesByUserId(userId);
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

    @RequestMapping("/user")
    public HR getUserDetailsAfterLogin(Authentication authentication) {
        HR optionalCustomer = userRepository.findByEmail(authentication.getName());
        return optionalCustomer;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> apiLogin (@RequestBody LoginRequestDTO loginRequest) {
        String jwt = "";
        System.out.println(loginRequest.username());
        System.out.println(loginRequest.password());
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),
                loginRequest.password());

        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != env) {
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder().issuer("Eazy Bank").subject("JWT Token")
                        .claim("username", authenticationResponse.getName())
                        .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .issuedAt(new java.util.Date())
                        .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                        .signWith(secretKey).compact();
            }else{
                System.out.println(env+"is not found");
            }

        }else{
            System.out.println(authenticationResponse+"is not found");
        }
        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER,jwt)
                .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }




}
