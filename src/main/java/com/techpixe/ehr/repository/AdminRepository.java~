package com.techpixe.ehr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.ehr.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Admin findByEmail(String email);

	Admin findByMobileNumber(Long mobileNumber);

	Admin findByPassword(String password);

}
