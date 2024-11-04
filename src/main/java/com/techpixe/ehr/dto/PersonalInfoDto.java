package com.techpixe.ehr.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PersonalInfoDto {
    private Long candidateId;

    private String Name;
    private String MobileNumber;
    private String EmailID;
    private int result;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private String jobRole;
    private String examId;
    private String status;
    private boolean examStatus;
}
