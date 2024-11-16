package com.techpixe.ehr.service;

import com.techpixe.ehr.dto.RegisterDto;
import com.techpixe.ehr.entity.Clients;
import org.springframework.web.multipart.MultipartFile;

public interface ClientsService {

    public RegisterDto registerEmployee(RegisterDto registerDto,Long id);

    public RegisterDto registerClients(RegisterDto registerDto);
    public Clients registerHRAndAdmin(String fullName, String email, Long mobileNumber, String role, MultipartFile logo,String companyName,String authorizedCompanyName,String address);

}
