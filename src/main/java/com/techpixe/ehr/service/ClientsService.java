package com.techpixe.ehr.service;

import com.techpixe.ehr.dto.RegisterDto;
import com.techpixe.ehr.entity.Clients;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ClientsService {

    public RegisterDto registerEmployee(RegisterDto registerDto,Long id);

    public Clients registerHRAndAdmin(String fullName, String email, Long mobileNumber, String role, MultipartFile logo,String companyName,String authorizedCompanyName,String address);
    public ResponseEntity<?> changePassword(Long Id, String password, String confirmPassword) ;
    public ResponseEntity<?> forgotPassword(String email);
    

}
