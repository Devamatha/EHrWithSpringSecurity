package com.techpixe.ehr.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techpixe.ehr.dto.ExamResultDto;
import com.techpixe.ehr.dto.PersonalInfoDto;
import com.techpixe.ehr.dto.PersonalInformationDto;
import com.techpixe.ehr.entity.*;
import com.techpixe.ehr.repository.PersonalInformationRepository;
import com.techpixe.ehr.repository.QuestionRepository;
import com.techpixe.ehr.repository.SubscriptionPlanRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.EmailService;
import com.techpixe.ehr.service.PersonalInformationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tika.Tika;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PersonalInformationServiceImpl implements PersonalInformationService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private PersonalInformationRepository personalInformationRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Value("$(spring.mail.username)")
    private String fromMail;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public PersonalInformation getPersonalInformationById(String emailid) {
        return personalInformationRepository.findByEmailID(emailid);
    }

    @Override
    public PersonalInformation savePersonalInformation(MultipartFile file, Long user_Id) throws Exception {
        HR user = userRepository.findById(user_Id).orElseThrow(() -> new RuntimeException(user_Id + " is not found"));
        Long subscriptions = subscriptionPlanRepository.findLatestSubscriptionIdByUserId(user.getUser_Id());
        PersonalInformation personalInformation = new PersonalInformation();
        Long TotalResumesCountcount = getCountByUserId(user_Id);
        System.err.println("TotalResumesCountcount" + TotalResumesCountcount);
        if (subscriptions != null) {
            SubscriptionPlan latestSubscription = subscriptionPlanRepository.findById(subscriptions).orElseThrow(() -> new RuntimeException(subscriptions + " Subscription plan not found"));
            System.out.println(latestSubscription.getPlanType() + " &&" + latestSubscription.getTotalResumes());
            try {
                if ("LifeTime".equals(latestSubscription.getPlanType()) && latestSubscription.getTotalResumes() <= TotalResumesCountcount) {

                    throw new SizeLimitExceededException("You have reached the maximum limit of resumes", latestSubscription.getTotalResumes(), TotalResumesCountcount);

                } else {
                    byte[] fileBytes = file.getBytes();
                    personalInformation.setResume(fileBytes);


                    String extractedText = "";
                    String allJobTitles = user.getAddJobDetails().stream().map(AddJobDetails::getJobTitle)
                            .collect(Collectors.joining(", "));
                    personalInformation.setUser(user);
                    personalInformation.setJobRole(allJobTitles);
                    //		personalInformation.setJobRole(jobDetails.getJobTitle());

                    if (file.getOriginalFilename().endsWith(".pdf")) {
                        try (PDDocument document = PDDocument.load(fileBytes)) {
                            PDFTextStripper pdfStripper = new PDFTextStripper();
                            extractedText = pdfStripper.getText(document);
                            // System.err.println(extractedText + "extractedText");

                            personalInformation.setResumeTextData(extractedText);

                        }
                    } else if (file != null && file.getOriginalFilename().endsWith(".docx")) {
                        try (XWPFDocument docx = new XWPFDocument(new ByteArrayInputStream(fileBytes))) {
                            XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
                            extractedText = extractor.getText();
                            personalInformation.setResumeTextData(extractedText);

                        }
                    } else if (file != null && file.getOriginalFilename().endsWith(".doc")) {
                        try (HWPFDocument doc = new HWPFDocument(new ByteArrayInputStream(fileBytes))) {
                            WordExtractor extractor = new WordExtractor(doc);
                            extractedText = extractor.getText();
                            personalInformation.setResumeTextData(extractedText);

                        }
                    } else {
                        // Fallback to Tika for other file types
                        Tika tika = new Tika();
                        extractedText = tika.parseToString(new ByteArrayInputStream(fileBytes));
                        // System.err.println(extractedText + "extractedText");

                        personalInformation.setResumeTextData(extractedText);

                    }
                }
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }

        }


        return personalInformationRepository.save(personalInformation);
    }

    @Override
    public Long getCountByUserId(Long userId) {
        return personalInformationRepository.countByUserId(userId);
    }

    @Override
    public ResponseEntity<String> savePersonalDateInformation(LocalDate date, LocalTime fromTime, String jobRole,
                                                              Long user_Id) throws Exception {
        // PersonalInformation personalInformation = new PersonalInformation();

        HR user = userRepository.findById(user_Id).orElseThrow(() -> new RuntimeException(user_Id + " is not found"));
        PersonalInformation personalInformation = personalInformationRepository
                .findTopByUserOrderByCandidateIdDesc(user);

        // System.err.println(personalInformation.getCandidateId() + "candidateId");
        personalInformation.setJobRole(jobRole);
        personalInformation.setInterviewDate(date);
        personalInformation.setInterviewTime(fromTime);
        personalInformation.getResumeTextData();
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder examIdBuilder = new StringBuilder();
        Random random = new Random();

        // Generate an 8-character alphanumeric string
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(alphanumeric.length());
            examIdBuilder.append(alphanumeric.charAt(index));
        }

        String examId = examIdBuilder.toString();
        personalInformation.setExamId(examId);
        PersonalInformation savedPersonalInformation = personalInformationRepository.save(personalInformation);
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("resumeTextData", personalInformation.getResumeTextData());
        jsonData.put("jobRole", jobRole);
        // System.err.println(jsonData+"jsonData");
        // Send the JSON data to the webhook URL
        ResponseEntity<String> webhookResponse = sendPostRequestToWebhook(jsonData);

        // Convert the webhook response JSON to DTO
        ObjectMapper objectMapper = new ObjectMapper();
        PersonalInformationDto webhookResponseDTO = objectMapper.readValue(webhookResponse.getBody(),
                PersonalInformationDto.class);

        savedPersonalInformation.setName(webhookResponseDTO.getPersonalInformation().getName());
        savedPersonalInformation.setMobileNumber(webhookResponseDTO.getPersonalInformation().getMobileNumber());
        savedPersonalInformation.setEmailID(webhookResponseDTO.getPersonalInformation().getEmailID());

        // Save the updated personal information with webhook data
        savedPersonalInformation = personalInformationRepository.save(savedPersonalInformation);
        saveQuestions(savedPersonalInformation, webhookResponseDTO.getQuestions(),
                personalInformation.getCandidateId());


        sendInterviewDetailsEmail(savedPersonalInformation);
        //emailService.sendEmail(to, subject, body);

        return new ResponseEntity<>(webhookResponse.getBody(), webhookResponse.getStatusCode());

    }


    private void sendInterviewDetailsEmail(PersonalInformation personalInformation)
            throws IOException, InterruptedException {
        try {
            String to = personalInformation.getEmailID();
            //System.out.println("sms method");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://gmail-checker4.p.rapidapi.com/GmailCheck?email="
                            + personalInformation.getEmailID()))
                    .header("x-rapidapi-key", "92dd61a3abmsh30aff286f1de18bp19ce46jsnd13651517f4e")
                    .header("x-rapidapi-host", "gmail-checker4.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            String subject = "Interview Details for the Job Role: " + personalInformation.getJobRole();
            String body = "Dear Candidate,\n\n" + "Thank you for applying for the position of "
                    + personalInformation.getJobRole() + ".\n" + "Your interview is scheduled on "
                    + personalInformation.getInterviewDate() + " at " + personalInformation.getInterviewTime()
                    + "Please join to this link  https://smartaihr.com/exam/" + personalInformation.getExamId()
                    + ".\n\n" + "Best regards,\nHR Team";
            sendEmail(to, subject, body);
            //emailService.sendEmail(to, subject, body);
        } catch (IOException | InterruptedException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public void sendEmail(String to, String subject, String body) {
        try {
            //System.out.println("sms method hgfhgf");

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromMail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        } catch (Exception e) {
            System.err.println("Unexpected error while sending email: " + e.getMessage());
            throw new RuntimeException("An error occurred while sending email", e);
        }
    }

    public void saveQuestions(PersonalInformation personalInformation, List<Question> questions, Long CandidateId) {
        for (Question question : questions) {
//	        Question question = new Question();
            question.setQuestion(question.getQuestion());
            question.setMinimumTime(question.getMinimumTime());
            question.setMaximumMarks(question.getMaximumMarks());
            question.setPersonalInformation(personalInformation);
            questionRepository.save(question);
        }
    }

    private ResponseEntity<String> sendPostRequestToWebhook(Map<String, Object> jsonData) {
        // Webhook URL
        String webhookUrl = "https://hook.eu2.make.com/p73auaerf1i2r67esein6l8nvtjuaowq";

        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HttpEntity containing the JSON data and headers
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonData, headers);

        // Send the POST request and return the response
        return restTemplate.postForEntity(webhookUrl, request, String.class);
    }

    public PersonalInformation getPersonalInformationByCandidateId(Long candidate_Id) {
        return personalInformationRepository.findById(candidate_Id).orElseThrow(
                () -> new RuntimeException("candidate not found for saving personal information " + candidate_Id));
    }

    public PersonalInformation savePersonalInformation(PersonalInformation personalInformation) {

        return personalInformationRepository.save(personalInformation);

    }

    public void saveQuestions(Long candidate_Id, List<Question> questions, PersonalInformation personalInformation) {
        for (Question question : questions) {
            question.setPersonalInformation(personalInformation);
            questionRepository.save(question);
        }

    }

    @Override
    public List<PersonalInformation> getAllPersonalInformation() {
        List<PersonalInformation> personalInformationList = personalInformationRepository.findAll();
        // Add logging here to verify the contents of the list
        return personalInformationList;
    }

    @Override
    public void saveAnswers(Long candidateId, ExamResultDto answers) {
        PersonalInformation candidate = personalInformationRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        String overallPercentageStr = answers.getOverallPercentage().replace("%", "").trim();
        int overallPercentage = Integer.parseInt(overallPercentageStr);

        candidate.setResult(overallPercentage);

        for (int i = 0; i < candidate.getQuestions().size(); i++) {
            Question oldQuestion = questionRepository.findById(candidate.getQuestions().get(i).getQuestion_Id())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            oldQuestion.setAnswer(answers.getAnswers().get(i));

            oldQuestion.setScore(overallPercentage);

            questionRepository.save(oldQuestion);
        }
    }

    @Override
    public boolean isVerified(String email) {
        System.err.println("Email received: " + email);

        // Check if PersonalInformation exists for the provided email
        PersonalInformation personalInformation = personalInformationRepository.findByEmailID(email);
        // System.err.println("personalInformation: " + personalInformation);

        if (personalInformation == null) {
            throw new RuntimeException("Email not found: " + email);
        }

        // System.err.println("PersonalInformation found with ID: " +
        // personalInformation.getCandidateId());

        ZonedDateTime currentZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalDate currentDate = currentZonedDateTime.toLocalDate();
        LocalTime currentTime = currentZonedDateTime.toLocalTime();
        // System.err.println("Current Date (Asia/Kolkata): " + currentDate);
        // System.err.println("Current Time (Asia/Kolkata): " + currentTime);

        LocalDateTime interviewDateTime = LocalDateTime.of(personalInformation.getInterviewDate(),
                personalInformation.getInterviewTime());

        ZonedDateTime interviewZonedDateTime = interviewDateTime.atZone(ZoneId.of("Asia/Kolkata"));
        LocalDate interviewDate = interviewZonedDateTime.toLocalDate();
        LocalTime interviewTime = interviewZonedDateTime.toLocalTime();

        // System.err.println("Interview Date: " + interviewDate);
        // System.err.println("Interview Time: " + interviewTime);

        // Calculate the duration between the interview time and the current time
        Duration duration = Duration.between(interviewTime, currentTime);

        // System.err.println("Interview Date: " +
        // personalInformation.getInterviewDate());
        // System.err.println("Interview Time: " +
        // personalInformation.getInterviewTime());

        // Check if current date matches the interview date
        if (currentDate.equals(personalInformation.getInterviewDate())) {
            // System.err.println("Date matches!");

            if (duration.isZero() || (duration.toMinutes() >= 0 && duration.toMinutes() <= 3)) {
                // System.err.println("Time is on or after the interview time.");
                return true;
            } else {
                // System.err.println("Current time is before the interview time.");
                throw new RuntimeException("Interview time mismatch for email: " + email);
            }
        } else {
            // System.err.println("Date does not match.");
            throw new RuntimeException("Interview date mismatch for email: " + email);
        }
    }

    @Override
    public List<Question> getAllQuestionsByPersonalInformationId(Long personalInformationId) {
        PersonalInformation personalInformation = personalInformationRepository.findById(personalInformationId)
                .orElseThrow(
                        () -> new RuntimeException("Personal Information not found with id: " + personalInformationId));
        return personalInformation.getQuestions();
    }

    @Override
    public PersonalInfoDto getPersonalInformationByexamId(String examId) {
        PersonalInformation personalInformation = personalInformationRepository.findByExamId(examId);
        PersonalInfoDto personalInfoDto = new PersonalInfoDto();

        if (personalInformation != null) {
            ZonedDateTime currentZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
            LocalDate currentDate = currentZonedDateTime.toLocalDate();
            LocalTime currentTime = currentZonedDateTime.toLocalTime();
            // System.err.println("Current Date (Asia/Kolkata): " + currentDate);
            // System.err.println("Current Time (Asia/Kolkata): " + currentTime);

            LocalDateTime interviewDateTime = LocalDateTime.of(personalInformation.getInterviewDate(),
                    personalInformation.getInterviewTime());

            ZonedDateTime interviewZonedDateTime = interviewDateTime.atZone(ZoneId.of("Asia/Kolkata"));
            LocalDate interviewDate = interviewZonedDateTime.toLocalDate();
            LocalTime interviewTime = interviewZonedDateTime.toLocalTime();

            // System.err.println("Interview Date: " + interviewDate);
            // System.err.println("Interview Time: " + interviewTime);

            // Calculate the duration between the interview time and the current time
            Duration duration = Duration.between(interviewTime, currentTime);

            // System.err.println("Interview Date: " +
            // personalInformation.getInterviewDate());
            // System.err.println("Interview Time: " +
            // personalInformation.getInterviewTime());
            // Check if current date matches the interview date
            personalInfoDto.setCandidateId(personalInformation.getCandidateId());
            personalInfoDto.setEmailID(personalInformation.getEmailID());
            personalInfoDto.setExamId(personalInformation.getExamId());
            personalInfoDto.setExamStatus(personalInformation.isExamStatus());
            personalInfoDto.setInterviewDate(personalInformation.getInterviewDate());
            personalInfoDto.setInterviewTime(personalInformation.getInterviewTime());
            personalInfoDto.setJobRole(personalInformation.getJobRole());
            personalInfoDto.setMobileNumber(personalInformation.getMobileNumber());
            personalInfoDto.setName(personalInformation.getName());
            personalInfoDto.setResult(personalInformation.getResult());
            personalInfoDto.setStatus(personalInformation.getStatus());
            if (currentDate.equals(personalInformation.getInterviewDate())) {
                // System.err.println("Date matches!");

                if (duration.isZero() || (duration.toMinutes() >= 0 && duration.toMinutes() <= 3)) {
                    // System.err.println("Time is on or after the interview time.");
                    personalInformation.setExamStatus(true);

                    personalInformationRepository.save(personalInformation);
                } else {
                    // System.err.println("Current time is before the interview time.");
                    throw new RuntimeException("Interview time mismatch for : " + personalInformation.getName()
                            + "  and examId  " + examId);
                }
            } else {
                // System.err.println("Date does not match.");
                throw new RuntimeException(
                        "Interview date mismatch for : " + personalInformation.getName() + "  and examId  " + examId);
            }
        } else {

            throw new RuntimeException("examId not found: " + examId);

        }

        return personalInfoDto;
    }

}

//	@Override
//	public PersonalInformation addPersonalInformation(Long id, String name, String mobileNumber, String emailID, List<Question> questions) {
//	    // Check if PersonalInformation with the given ID exists
//	    Optional<PersonalInformation> optionalPersonalInformation = personalInformationRepository.findById(id);
//
//	    if (optionalPersonalInformation.isPresent()) {
//	        PersonalInformation personalInformation = optionalPersonalInformation.get();
//	        personalInformation.setName(name);
//	        personalInformation.setMobileNumber(mobileNumber);
//	        personalInformation.setEmailID(emailID);
//
//	        // Clear the existing questions to avoid duplicates
//	        personalInformation.getQuestions().clear();
//	        
//	        personalInformation.getQuestions().stream()
//                    .map(dto -> {
//                        Question question = new Question();
//                        question.setQuestion(question.getQuestion());
//                        question.setMinimumTime(dto.getMinimumTime());
//                        return question;
//                    }).collect(Collectors.toList());
//	        
////	      
////	        for (Question question : questions) {
////
////	            question.setPersonalInformation(personalInformation);
////	            // Add the question to the PersonalInformation's list
////	            personalInformation.getQuestions().add(question);
////	        }
//
//	        // Save updated PersonalInformation
//	        personalInformation = personalInformationRepository.save(personalInformation);
//
//	        return personalInformation;
//	    } else {
//	        // Handle case where the PersonalInformation with the given ID does not exist
//	        throw new RuntimeException("Personal Information not found for ID: " + id);
//	    }
//	}
//
//
//	
//}
//	@Override
//	public PersonalInformation addPersonalInformation(Long id, String name, String mobileNumber, String emailID,
//			List<Question> questions) {
//		// Check if PersonalInformation with the given ID exists
//		Optional<PersonalInformation> optionalPersonalInformation = personalInformationRepository.findById(id);
//
//		PersonalInformation personalInformation;
//		if (optionalPersonalInformation.isPresent()) {
//			// Update existing PersonalInformation
//			personalInformation = optionalPersonalInformation.get();
//			personalInformation.setName(name);
//			personalInformation.setMobileNumber(mobileNumber);
//			personalInformation.setEmailID(emailID);
//
//			// Save updated PersonalInformation
//			personalInformation = personalInformationRepository.save(personalInformation);
//
//			// Clear existing questions if any
//			//questionRepository.deleteByPersonalInformationId(id);
////		} else {
////			// Create a new PersonalInformation
////			personalInformation = new PersonalInformation();
////			personalInformation.setName(name);
////			personalInformation.setMobileNumber(mobileNumber);
////			personalInformation.setEmailID(emailID);
////
////			// Save new PersonalInformation
////			personalInformation = personalInformationRepository.save(personalInformation);
////		}
//
//		// Associate questions with the PersonalInformation
//		for (Question question : questions) {
//			question.setPersonalInformation(personalInformation);
//			//personalInformation.setQuestions(question.getQuestion_Id());
//			questionRepository.save(question);
//		}
//
//		return personalInformation;
//	}
