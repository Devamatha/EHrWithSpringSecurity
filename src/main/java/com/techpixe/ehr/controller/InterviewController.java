package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.ExamResultDto;
import com.techpixe.ehr.entity.Interview;
import com.techpixe.ehr.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    @Autowired
    InterviewService interviewService;

    @PostMapping("/send/{candidate_Id}")
    public ResponseEntity<?> sendInterviewDetails(@PathVariable Long candidate_Id,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate interviewDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime interviewTime) {

        Interview interviewDetails = new Interview();
        interviewDetails.setInterviewDate(interviewDate);
        interviewDetails.setInterviewTime(interviewTime);
        // interviewDetails.setEndTime(endTime);

        interviewService.sendInterviewDetails(candidate_Id, interviewDetails);
        // return new ResponseEntity<>("Interview details sent successfully",
        // HttpStatus.OK);
        return new ResponseEntity<>(interviewDetails, HttpStatus.OK);
    }

    @PutMapping("/update/{candidate_Id}")
    public ResponseEntity<?> updateInterviewByCandidateId(@PathVariable Long candidate_Id,
                                                          @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate interviewDate,
                                                          @RequestParam @DateTimeFormat(iso = ISO.TIME) LocalTime interviewTime) {

        interviewService.updateInterviewByCandidateId(candidate_Id, interviewDate, interviewTime);
        return new ResponseEntity<>("Interview details updated successfully", HttpStatus.OK);
    }

    @PostMapping("/save-answers/{candidate_Id}")
    public ResponseEntity<?> saveCandidateAnswers(@PathVariable Long candidate_Id,
                                                  @RequestBody ExamResultDto finalOutput) throws IOException, InterruptedException {

        interviewService.saveAnswers(candidate_Id, finalOutput);

        return new ResponseEntity<>("Answers saved successfully", HttpStatus.OK);
    }
}
