package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long question_Id;

    private String question;
    private int minimumTime;

    private String answer;

    private int maximumMarks;

    private int score;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "candidate_Id")
    private PersonalInformation personalInformation;

}
