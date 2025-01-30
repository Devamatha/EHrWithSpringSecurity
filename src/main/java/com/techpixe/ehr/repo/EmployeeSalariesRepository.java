package com.techpixe.ehr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.ehr.sentity.EmployeeSalaries;

public interface EmployeeSalariesRepository extends JpaRepository<EmployeeSalaries, Long> {

}
