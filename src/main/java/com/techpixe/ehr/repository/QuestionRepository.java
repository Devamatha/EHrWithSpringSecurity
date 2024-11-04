package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // void deleteByPersonalInformationId(Long id);

}
