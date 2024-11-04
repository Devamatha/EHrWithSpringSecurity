package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.AddJobDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddJobDetailsRepository extends JpaRepository<AddJobDetails, Long> {

}