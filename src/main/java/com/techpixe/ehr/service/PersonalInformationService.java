package com.techpixe.ehr.service;

import com.techpixe.ehr.dto.ExamResultDto;
import com.techpixe.ehr.dto.PersonalInfoDto;
import com.techpixe.ehr.entity.PersonalInformation;
import com.techpixe.ehr.entity.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PersonalInformationService {


//	PersonalInformation addPersonalInformation(Long candidateId, String name, String mobileNumber, String emailID,
//			List<Question> questions);

    PersonalInformation savePersonalInformation(PersonalInformation personalInfo);

    void saveQuestions(Long candidate_Id, List<Question> questions, PersonalInformation savedPersonalInfo);

    PersonalInformation getPersonalInformationByCandidateId(Long candidate_Id);

    List<PersonalInformation> getAllPersonalInformation();

    PersonalInformation savePersonalInformation(MultipartFile file,
                                                Long user_Id) throws Exception;


    ResponseEntity<String> savePersonalDateInformation(LocalDate date, LocalTime fromTime, String jobRole,
                                                       Long user_Id) throws Exception;

    void saveAnswers(Long candidateId, ExamResultDto answers);

    boolean isVerified(String email);

    PersonalInformation getPersonalInformationById(String emailid);

    PersonalInfoDto getPersonalInformationByexamId(String examId);

    List<Question> getAllQuestionsByPersonalInformationId(Long personalInformationId);

    public Long getCountByUserId(Long userId);

}
