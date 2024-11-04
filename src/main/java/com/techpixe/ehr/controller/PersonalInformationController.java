package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.PersonalInfoDto;
import com.techpixe.ehr.dto.PersonalInformationDto;
import com.techpixe.ehr.entity.PersonalInformation;
import com.techpixe.ehr.entity.Question;
import com.techpixe.ehr.service.EmailService;
import com.techpixe.ehr.service.PersonalInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class PersonalInformationController {

    @Autowired
    private PersonalInformationService personalInformationService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/upload-resume/{user_id}")
    public PersonalInformation  uploadResume(@RequestParam(value = "file", required = false) MultipartFile file,

                                          @PathVariable Long user_id) throws Exception {

       PersonalInformation savedPersonalInformation = personalInformationService.savePersonalInformation(file,
                    user_id);
        return savedPersonalInformation;

    }


    @PutMapping("/update-Details/{user_id}")
    public ResponseEntity<?> uploadDate(
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(value = "fromTime", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime fromTime,
            @RequestParam String jobRole, @PathVariable Long user_id) {
        try {
            // Get the response from the webhook
            ResponseEntity<String> webhookResponse = personalInformationService.savePersonalDateInformation(date,
                    fromTime, jobRole, user_id);

            //System.err.println(webhookResponse.getBody());
            // System.err.println(webhookResponse.getBody());

            // Return the webhook response directly
            return webhookResponse;

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update details: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add/{candidate_Id}")
    public ResponseEntity<?> addPersonalInformation(@PathVariable Long candidate_Id,
                                                    @RequestBody PersonalInformationDto payload) {
        if (payload == null || payload.getPersonalInformation() == null) {
            return new ResponseEntity<>("Invalid request payload", HttpStatus.BAD_REQUEST);
        }

        PersonalInformation personalInfo = personalInformationService.getPersonalInformationByCandidateId(candidate_Id);

        //System.err.println(personalInfo);
        personalInfo.setName(payload.getPersonalInformation().getName());
        personalInfo.setMobileNumber(payload.getPersonalInformation().getMobileNumber());
        personalInfo.setEmailID(payload.getPersonalInformation().getEmailID());
        personalInfo.setInterviewDate(payload.getPersonalInformation().getInterviewDate());
        personalInfo.setInterviewTime(payload.getPersonalInformation().getInterviewTime());

        personalInfo = personalInformationService.savePersonalInformation(personalInfo);

        if (payload.getQuestions() != null) {
            personalInformationService.saveQuestions(candidate_Id, payload.getQuestions(), personalInfo);
        }

        // return new ResponseEntity<>("Request processed successfully", HttpStatus.OK);
        return new ResponseEntity<>(personalInfo, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PersonalInformation>> getAllPersonalInformation() {
        List<PersonalInformation> personalInfoList = personalInformationService.getAllPersonalInformation();
        return new ResponseEntity<>(personalInfoList, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<PersonalInformation> getPersonalInformationByString(@PathVariable("email") String email) {
        PersonalInformation personalInformation = personalInformationService.getPersonalInformationById(email);
        if (personalInformation != null) {
            return new ResponseEntity<>(personalInformation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/exam/{examId}")
    public ResponseEntity<PersonalInfoDto> getPersonalInformation(@PathVariable("examId") String examId) {
        PersonalInfoDto personalInformation = personalInformationService.getPersonalInformationByexamId(examId);
        if (personalInformation != null) {
            return new ResponseEntity<>(personalInformation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> getVerified(@RequestParam("email") String email) {
        //System.err.println(email);
        boolean personalInfoList = personalInformationService.isVerified(email);
        return new ResponseEntity<>(personalInfoList, HttpStatus.OK);
    }

    @GetMapping("/{id}/questions")
    public List<Question> getAllQuestionsByPersonalInformationId(@PathVariable("id") Long personalInformationId) {
        return personalInformationService.getAllQuestionsByPersonalInformationId(personalInformationId);
    }

    @GetMapping("/send-message")
    public void sendMessage() {
        emailService.sendEmail("p.devamatha2001@gmail.com", "hsi", "https://zipdatainformation.s3.amazonaws.com/2/images.zip");
//		return otpService.sendMessage();

    }


}
//	    @PostMapping("/add/{candidate_Id}")
//	    public ResponseEntity<String> handleRequest(@PathVariable Long candidate_Id,@RequestBody PersonalInformationDto payload) {
//	        // Create and save PersonalInformation
//	    	System.err.println(payload.getPersonalInformation());
//	        PersonalInformation personalInfo = new PersonalInformation();
//	        personalInfo.getName();
//	        personalInfo.getEmailID();
//	        personalInfo.getMobileNumber();
//	        personalInfo.getQuestions();
//	        // Save PersonalInformation first
//	        PersonalInformation savedPersonalInfo = personalInformationService.savePersonalInformation(payload.getPersonalInformation());
//
//	        // Save associated questions
//	        personalInformationService.saveQuestions(payload.getQuestions(), savedPersonalInfo);
//
//	        return new ResponseEntity<>("Request processed successfully", HttpStatus.OK);
//	    }
//}
//	    @PostMapping("/add/{candidateId}")
//	    public ResponseEntity<PersonalInformation> addPersonalInformation(
//	            @PathVariable("candidate_Id") Long candidateId,
//	            @RequestBody PersonalInformation request) {
//	        try {
//	        	System.err.println(request);
//	            PersonalInformation updatedPersonalInformation = personalInformationService.addPersonalInformation(
//	                    candidateId,
//	                    request.getName(),
//	                    request.getMobileNumber(),
//	                    request.getEmailID(),
//	                    request.getQuestions()
//	            );
//	            
//	            return new ResponseEntity<>(updatedPersonalInformation, HttpStatus.CREATED);
//	        } catch (Exception e) {
//	            // Log the exception for further debugging
//	            System.out.println("Failed to add personal information");
//	            e.printStackTrace();
//	            
//	            // Return a structured error response
//	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	        }
//	    }
//
//}
//	    @PostMapping("/add/{candidate_Id}")
//	    public ResponseEntity<String> updatePersonalInformation(
//	            @PathVariable("id") Long id,
//	            @RequestParam("name") String name,
//	            @RequestParam("mobileNumber") String mobileNumber,
//	            @RequestParam("emailID") String emailID,
//	            @RequestParam("questions") String questionsJson) { // Expect JSON string for questions
//	        try {
//	            // Convert questionsJson to List<Question>
//	            List<Question> questions = parseQuestions(questionsJson);
//
//	            PersonalInformation updatedPersonalInformation = personalInformationService.addPersonalInformation(id, name, mobileNumber, emailID, questions);
//	            return new ResponseEntity<>("Personal Information added successfully for ID: " + updatedPersonalInformation.getCandidate_Id(), HttpStatus.OK);
//	        } catch (Exception e) {
//	            return new ResponseEntity<>("Failed to update Personal Information: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//	        }
//	    }
//	    private List<Question> parseQuestions(String questionsJson) {
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        try {
//	            // Deserialize JSON string to List<Question>
//	            return objectMapper.readValue(questionsJson, new TypeReference<List<Question>>(){});
//	        } catch (Exception e) {
//	            throw new RuntimeException("Failed to parse questions JSON", e);
//	        }
//	    }
//	
//       
//}
