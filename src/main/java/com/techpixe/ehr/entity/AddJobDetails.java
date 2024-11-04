package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddJobDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long jobId;

    private String jobTitle;

    //private List<String> jobkeyskills;

    private LocalDate createdAt;

    private int yearsOfExperience;
    private int noOfVacancies;

    private String overallPercentage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id")
    @JsonBackReference
    private HR user;


}
