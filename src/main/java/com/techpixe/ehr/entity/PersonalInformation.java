package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
////@Table(name = "personal_information")
public class PersonalInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Lob
    @Column(columnDefinition = "TEXT")
    private String resumeTextData;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] resume; // Store the resume file as a byte array

    // @JsonIgnoreProperties("personalInformation")
    @OneToMany(mappedBy = "personalInformation", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @JsonManagedReference
    private List<Question> Questions = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "interview_Id")
    private Interview interview;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id")
    @JsonBackReference
    private HR user;

}
