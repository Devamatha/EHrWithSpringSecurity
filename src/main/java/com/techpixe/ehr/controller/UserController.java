package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.MailDto;
import com.techpixe.ehr.entity.*;
import com.techpixe.ehr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;


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
	public List<Map<String, Object>> getEmployeesByUserId(@PathVariable Long userId) {
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
	public List<Map<String, Object>> getPersonalInformationByUserId(@PathVariable Long userId) {
		List<PersonalInformation> CandidateDetails = userService.getPersonalInformationByUserId(userId);
		List<Map<String, Object>> response = new ArrayList<>();
		for (PersonalInformation candidate : CandidateDetails) {
			if(candidate.getEmailID()!=null && candidate.getInterviewDate()!=null && candidate.getInterviewTime()!=null) {
				Map<String, Object> candidateData = new HashMap<>();

				candidateData.put("candidateId", candidate.getCandidateId());
				candidateData.put("result", candidate.getResult());
				candidateData.put("examId", candidate.getExamId());
				candidateData.put("mobileNumber", candidate.getMobileNumber());
				candidateData.put("emailID", candidate.getEmailID());
				candidateData.put("name", candidate.getName());
				candidateData.put("interviewDate", candidate.getInterviewDate());
				candidateData.put("interviewTime", candidate.getInterviewTime());
				candidateData.put("status", candidate.getStatus());
				candidateData.put("jobRole", candidate.getJobRole());
				response.add(candidateData);
			}
			
		}
		return response;
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

	@PostMapping("/sendEmail")
	public MailDto sendEmail(@RequestParam String fullName, @RequestParam String email, @RequestParam String category,
			@RequestParam String subCategory, @RequestParam String message, @RequestParam long phoneNumber) {
		MailDto mailDto = new MailDto();
		System.err.println(fullName + "fullname");

		JavaMailSender javaMailSender = configureMailSender();

		// Create the email message
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("sales@thewisguystech.com");
		simpleMailMessage.setTo("sales@thewisguystech.com");
		simpleMailMessage.setSubject("Request for " + category + "/" + subCategory);
		simpleMailMessage.setText("I hope this message finds you well. My name is " + fullName
				+ ",\n\nand I am reaching out regarding " + category + "/" + subCategory + "\nMessage: " + message
				+ "\nPhone Number: " + phoneNumber + "\nEmail: " + email);

		// Send the email
		javaMailSender.send(simpleMailMessage);

		return mailDto;
	}

	private JavaMailSender configureMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.hostinger.com");
		mailSender.setPort(465);
		mailSender.setUsername("sales@thewisguystech.com");
		mailSender.setPassword("Bstore@9652");
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.debug", "true");

		// Disable certificate validation (Not recommended for production)
		props.put("mail.smtp.ssl.trust", "smtp.hostinger.com");

		return mailSender;
	}

}
