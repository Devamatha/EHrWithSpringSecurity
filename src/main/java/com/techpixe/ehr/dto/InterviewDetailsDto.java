package com.techpixe.ehr.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class InterviewDetailsDto {

    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private LocalTime endTime;

    public InterviewDetailsDto() {
        super();
    }

    public InterviewDetailsDto(LocalDate interviewDate, LocalTime interviewTime, LocalTime endTime) {
        super();
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.endTime = endTime;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }

    public LocalTime getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(LocalTime interviewTime) {
        this.interviewTime = interviewTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

}
