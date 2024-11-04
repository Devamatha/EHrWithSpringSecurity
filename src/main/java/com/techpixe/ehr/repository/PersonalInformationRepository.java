package com.techpixe.ehr.repository;


import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.entity.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonalInformationRepository extends JpaRepository<PersonalInformation, Long> {
    PersonalInformation findTopByUserOrderByUserDesc(HR user);

    PersonalInformation findTopByUserOrderByCandidateIdDesc(HR user);

    @Query("SELECT p FROM PersonalInformation p WHERE p.EmailID = :EmailID")
    PersonalInformation findByEmailID(String EmailID);

    @Query("SELECT p FROM PersonalInformation p WHERE p.examId = :examId")
    PersonalInformation findByExamId(String examId);

    @Query("SELECT COUNT(p) FROM PersonalInformation p WHERE p.user.user_Id = :userId")
    Long countByUserId(@Param("userId") Long userId);
}
