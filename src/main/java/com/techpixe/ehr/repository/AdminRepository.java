package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);

    Admin findByMobileNumber(Long mobileNumber);

    Admin findByPassword(String password);

}
