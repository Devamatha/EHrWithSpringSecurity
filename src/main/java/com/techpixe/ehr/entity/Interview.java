package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interview_Id;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private LocalTime endTime;

    @JsonManagedReference
    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonalInformation> candidates = new ArrayList<>();

}
