package com.techpixe.ehr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long questionId;
    private String answer;
    private int score;

}
