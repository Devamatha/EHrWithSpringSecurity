package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.ErrorResponseDto;
import com.techpixe.ehr.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private com.techpixe.ehr.service.AdminService adminService;

    @PostMapping("/registration")
    public ResponseEntity<Admin> registerAdmin(@RequestParam String fullName, @RequestParam String email,
                                               @RequestParam Long mobileNumber, @RequestParam String password) {


        Admin registeredAdmin = adminService.registerAdmin(fullName, email, mobileNumber, password);
        return new ResponseEntity<Admin>(registeredAdmin, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String emailOrMobileNumber, @RequestParam String password) {
        if (emailOrMobileNumber != null) {
            if (isEmail(emailOrMobileNumber)) {
                return adminService.loginByEmail(emailOrMobileNumber, password);
            } else if (isMobileNumber(emailOrMobileNumber)) {
                return adminService.loginByMobileNumber(Long.parseLong(emailOrMobileNumber), password);
            } else {
                ErrorResponseDto errorResponse = new ErrorResponseDto();
                errorResponse
                        .setError("Invalid emailOrMobileNumber format. Please provide a valid email or mobile number.");
                return ResponseEntity.internalServerError().body(errorResponse);
            }
        } else {
            ErrorResponseDto errorResponse = new ErrorResponseDto();
            errorResponse.setError("Invalid input. Email or mobile number must be provided.");
            return ResponseEntity.internalServerError().body(errorResponse);
        }

    }

    private boolean isEmail(String emailOrMobileNumber) {
        return emailOrMobileNumber.contains("@");
    }

    private boolean isMobileNumber(String emailOrMobileNumber) {
        return emailOrMobileNumber.matches("\\d+");
    }


    // ************Change Password***************
//	@PostMapping("/changepassword/{admin_Id}")
//	public ResponseEntity<?> changePassword(@PathVariable Long admin_Id, @RequestParam String password,
//			@RequestParam String confirmPassword) {
//		if (password != null && confirmPassword != null) {
//			return adminService.changePassword(admin_Id, password, confirmPassword);
//		} else {
//			ErrorResponseDto error = new ErrorResponseDto();
//			error.setError("*********Password is not present*************");
//			return ResponseEntity.internalServerError().body(error);
//		}
//	}

    // *************FORGOT PASSWORD****************
//	@PostMapping("/forgotPassword")
//	public ResponseEntity<?> forgotPassword(@RequestParam String email) {
//		if (email != null) {
//			if (isEmail(email)) {
//				return adminService.forgotPassword(email);
//			} else {
//				ErrorResponseDto error = new ErrorResponseDto();
//				error.setError("*********Invalid Email Pattern *************");
//				return ResponseEntity.internalServerError().body(error);
//			}
//		} else {
//			ErrorResponseDto error = new ErrorResponseDto();
//			error.setError("*********Email is not present*************");
//			return ResponseEntity.internalServerError().body(error);
//		}
//	}


    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Optional<Admin> admin = adminService.getAdminById(id);
        if (admin.isPresent()) {
            return ResponseEntity.ok(admin.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
