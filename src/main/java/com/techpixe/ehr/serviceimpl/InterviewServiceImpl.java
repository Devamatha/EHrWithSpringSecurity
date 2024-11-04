package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.dto.ExamResultDto;
import com.techpixe.ehr.entity.Interview;
import com.techpixe.ehr.entity.PersonalInformation;
import com.techpixe.ehr.entity.Question;
import com.techpixe.ehr.repository.InterviewRepository;
import com.techpixe.ehr.repository.PersonalInformationRepository;
import com.techpixe.ehr.repository.QuestionRepository;
import com.techpixe.ehr.service.EmailService;
import com.techpixe.ehr.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private PersonalInformationRepository personalInformationRepository;
    @Autowired
    private PersonalInformationServiceImpl personalInformationServiceImpl;
    @Autowired
    private EmailService emailService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private InterviewRepository interviewRepository;

    @Override
    public void sendInterviewDetails(Long candidateId, Interview interviewDetails) {
        PersonalInformation candidate = personalInformationRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        int totalDurationInSeconds = 0;
        List<Question> questions = candidate.getQuestions(); // Assuming you have a method to get questions
        for (Question question : questions) {
            totalDurationInSeconds += question.getMinimumTime();
        }
        LocalTime interviewStartTime = interviewDetails.getInterviewTime();
        LocalTime interviewEndTime = interviewStartTime.plusSeconds(totalDurationInSeconds);
        // Set the end time in the DTO
        interviewDetails.setEndTime(interviewEndTime);

        // Prepare the message

        String message = String.format("I hope this message finds you well.\r\n" + "\r\n"
                        + "Thank you for applying for the [Position Title] role at techpixe. We are impressed with your background and would like to invite you to an interview to discuss your qualifications and the opportunity further.\r\n"
                        + "\r\n"
                        + "Could you please provide your availability for a [phone/virtual/in-person] interview over the next few days? We are flexible and happy to accommodate a time that works best for you.\r\n"
                        + "\r\n"
                        + "Please let us know your preferred times, and if you have any specific questions or need additional information prior to the interview, feel free to reach out.\r\n"
                        + "\r\n" + "you have an interview scheduled on %s at %s.\r\n" + "\r\n"
                        + "We look forward to speaking with you soon.", candidate.getName(),
                interviewDetails.getInterviewDate(), interviewDetails.getInterviewTime(),
                interviewDetails.getEndTime());
//	        String message = String.format("Dear %s, you have an interview scheduled on %s at %s. The questions will be timed and will end at %s.",
//	                candidate.getName(), interviewDetails.getInterviewDate(), interviewDetails.getInterviewTime(), interviewDetails.getEndTime());

        // Send email
        emailService.sendEmail(candidate.getEmailID(), "Interview Invitation at Techpixe", message);

        Interview interview = interviewRepository.save(interviewDetails);

        // PersonalInformation personalInformation=new PersonalInformation();

        candidate.setInterview(interview);
        personalInformationRepository.save(candidate);

    }

    @Override
    public void updateInterviewByCandidateId(Long candidate_Id, LocalDate interviewDate, LocalTime interviewTime) {
        PersonalInformation candidate = personalInformationRepository.findById(candidate_Id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Interview existingInterview = candidate.getInterview();
        if (existingInterview == null) {
            throw new RuntimeException("Interview not found for the candidate");
        }

        existingInterview.setInterviewDate(interviewDate);
        existingInterview.setInterviewTime(interviewTime);
        int totalDurationInSeconds = 0;
        List<Question> questions = candidate.getQuestions(); // Assuming you have a method to get questions
        for (Question question : questions) {
            totalDurationInSeconds += question.getMinimumTime();
        }
        LocalTime interviewStartTime = existingInterview.getInterviewTime();
        LocalTime interviewEndTime = interviewStartTime.plusSeconds(totalDurationInSeconds);
        // Set the end time in the DTO
        existingInterview.setEndTime(interviewEndTime);
        existingInterview.setEndTime(interviewEndTime);

        interviewRepository.save(existingInterview);
    }

    @Override
    public void saveAnswers(Long candidateId, ExamResultDto answers) throws IOException, InterruptedException {
        PersonalInformation candidate = personalInformationRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        String overallPercentageStr = answers.getOverallPercentage().replace("%", "").trim();
        int overallPercentage = Integer.parseInt(overallPercentageStr);
        // System.err.println(answers.getDecision() + "answers.getDecision()");
        // System.err.println(answers.getJustification() +
        // "answers.getJustification()");
        // System.out.println(answers.getOverallPercentage() +
        // "answers.getOverallPercentage");
        // System.out.println(candidate.getEmailID() + "emailId");
        candidate.setResult(overallPercentage);
        candidate.setStatus(answers.getDecision());
        for (int i = 0; i < candidate.getQuestions().size(); i++) {
            Question oldQuestion = questionRepository.findById(candidate.getQuestions().get(i).getQuestion_Id())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            oldQuestion.setAnswer(answers.getAnswers().get(i));

            oldQuestion.setScore(overallPercentage);

            questionRepository.save(oldQuestion);

        }
        sendInterviewDetailsEmail(candidate, answers);

    }

    private void sendInterviewDetailsEmail(PersonalInformation candidateId, ExamResultDto answers)
            throws IOException, InterruptedException {
        try {
            String to = candidateId.getEmailID();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://gmail-checker4.p.rapidapi.com/GmailCheck?email=" + candidateId.getEmailID()))
                    .header("x-rapidapi-key", "92dd61a3abmsh30aff286f1de18bp19ce46jsnd13651517f4e")
                    .header("x-rapidapi-host", "gmail-checker4.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            String subject = "Interview FeedBack: " + answers.getDecision();
            String body = "Dear" + candidateId.getName() + ",\n\n" + "Thank you for applying for the position of "
                    + candidateId.getJobRole() + " at " + candidateId.getUser().getCompanyName() + ".\n\n"
                    + "Here is your interview feedback:\n" + "Decision: " + answers.getDecision() + "\n"
                    + "Overall Percentage: " + answers.getOverallPercentage() + "\n" + "Justification: "
                    + answers.getJustification() + "\n\n" + "Your interview was completed on "
                    + candidateId.getInterviewDate() + " at " + candidateId.getInterviewTime() + ".\n\n"
                    + "Best regards,\n" + candidateId.getUser().getCompanyName() + " HR Team";

            personalInformationServiceImpl.sendEmail(to, subject, body);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
