package com.techpixe.ehr.dto;

import com.techpixe.ehr.entity.PersonalInformation;
import com.techpixe.ehr.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class PersonalInformationDto {
    private PersonalInformation personalInformation;
    private List<Question> Questions = new ArrayList<>();

    public PersonalInformationDto() {
        super();
    }

    public PersonalInformationDto(PersonalInformation personalInformation, List<Question> questions) {
        super();
        this.personalInformation = personalInformation;
        Questions = questions;
    }

    public PersonalInformation getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(PersonalInformation personalInformation) {
        this.personalInformation = personalInformation;
    }

    public List<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<Question> questions) {
        Questions = questions;
    }

}
