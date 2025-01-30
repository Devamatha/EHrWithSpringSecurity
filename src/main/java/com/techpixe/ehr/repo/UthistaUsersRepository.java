package com.techpixe.ehr.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.ehr.sentity.UthistaUsers;

public interface UthistaUsersRepository extends JpaRepository<UthistaUsers, Long> {
	UthistaUsers findByEmail(String email);
}
