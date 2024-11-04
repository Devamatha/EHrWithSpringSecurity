package com.techpixe.ehr.dto;

import java.time.LocalDate;
import java.util.List;

public class AddJobDetailsDto {
    private String jobTitle;
    private List<String> jobkeyskills;
    private LocalDate createdAt;
    private int yearsOfExperience;
    private int noOfVacancies;
    private String overallPercentage;

    public AddJobDetailsDto() {
        super();
    }

    public AddJobDetailsDto(String jobTitle, List<String> jobkeyskills, LocalDate createdAt, int yearsOfExperience,
                            int noOfVacancies, String overallPercentage) {
        super();
        this.jobTitle = jobTitle;
        this.jobkeyskills = jobkeyskills;
        this.createdAt = createdAt;
        this.yearsOfExperience = yearsOfExperience;
        this.noOfVacancies = noOfVacancies;
        this.overallPercentage = overallPercentage;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<String> getJobkeyskills() {
        return jobkeyskills;
    }

    public void setJobkeyskills(List<String> jobkeyskills) {
        this.jobkeyskills = jobkeyskills;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getNoOfVacancies() {
        return noOfVacancies;
    }

    public void setNoOfVacancies(int noOfVacancies) {
        this.noOfVacancies = noOfVacancies;
    }

    public String getOverallPercentage() {
        return overallPercentage;
    }

    public void setOverallPercentage(String overallPercentage) {
        this.overallPercentage = overallPercentage;
    }

}
