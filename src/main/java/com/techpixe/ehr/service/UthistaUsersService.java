package com.techpixe.ehr.service;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

public interface UthistaUsersService {
	ResponseEntity<?> loginByEmail(String email, String password)throws BadRequestException;

}
