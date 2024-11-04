package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.Admin;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AdminService {
    Admin registerAdmin(String fullName, String email, Long mobileNumber, String password);

    ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password);

    ResponseEntity<?> loginByEmail(String email, String password);
//
//	ResponseEntity<?> changePassword(Long admin_Id, String password, String confirmPassword);
//
//	// ********FORGOT PASSWORD************
//	ResponseEntity<?> forgotPassword(String email);

    Optional<Admin> getAdminById(Long id);
}
