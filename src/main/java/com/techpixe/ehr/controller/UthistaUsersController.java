package com.techpixe.ehr.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.ehr.service.UthistaUsersService;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/uthista")
@Validated
public class UthistaUsersController {

	@Autowired
	private UthistaUsersService uthistaUsersService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam @Email(message = "Invalid email") String email,
			@RequestParam @NotBlank(message = "Password is required") String password) throws BadRequestException {
		return uthistaUsersService.loginByEmail(email, password);
	}

}
