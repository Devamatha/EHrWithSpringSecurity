package com.techpixe.ehr.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.ehr.sentity.Site_users;

public interface Site_UsersRepository extends JpaRepository<Site_users, Long> {
	Optional<Site_users> findByDateAndEmployeeId(LocalDate date, long employeeId);
}
