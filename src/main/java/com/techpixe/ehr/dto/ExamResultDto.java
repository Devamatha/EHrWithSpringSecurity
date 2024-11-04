package com.techpixe.ehr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDto {
    @JsonProperty("Decision")
    private String Decision;

    @JsonProperty("Justification")
    private String Justification;

    @JsonProperty("OverallPercentage")
    private String OverallPercentage;

    @JsonProperty("QuestionScores")
    private Map<String, Integer> QuestionScores;

    @JsonProperty("answers")
    private List<String> answers;
}
