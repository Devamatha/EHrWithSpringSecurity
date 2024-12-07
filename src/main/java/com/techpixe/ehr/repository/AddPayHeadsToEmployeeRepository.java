package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.AddPayHeadsToEmployee;
import com.techpixe.ehr.entity.EmployeeTable;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddPayHeadsToEmployeeRepository extends JpaRepository<AddPayHeadsToEmployee, Long> {

	Optional<AddPayHeadsToEmployee> findByEmployeeTableAndSelectedPayHead(EmployeeTable employeeTable,
			String selectedPayHead);

}
