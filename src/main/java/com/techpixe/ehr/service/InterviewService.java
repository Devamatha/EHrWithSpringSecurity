package com.techpixe.ehr.service;

import com.techpixe.ehr.dto.ExamResultDto;
import com.techpixe.ehr.entity.Interview;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public interface InterviewService {

    void sendInterviewDetails(Long candidateId, Interview interviewDetails);

    void updateInterviewByCandidateId(Long candidate_Id, LocalDate interviewDate, LocalTime interviewTime);

    void saveAnswers(Long candidateId, ExamResultDto answers) throws IOException, InterruptedException;

}
