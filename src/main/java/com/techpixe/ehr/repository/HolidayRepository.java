package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

}
