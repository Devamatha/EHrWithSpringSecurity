package com.techpixe.ehr.service;

import com.techpixe.ehr.dto.RegisterDto;
import org.springframework.web.multipart.MultipartFile;

public interface ClientsService {

    public RegisterDto registerEmployee(RegisterDto registerDto,Long id);

    public RegisterDto registerClients(RegisterDto registerDto);

}
