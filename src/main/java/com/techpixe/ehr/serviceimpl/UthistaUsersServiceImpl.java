package com.techpixe.ehr.serviceimpl;

import javax.management.RuntimeErrorException;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.techpixe.ehr.constant.ApplicationConstants;
import com.techpixe.ehr.dto.ErrorResponseDto;
import com.techpixe.ehr.dto.LoginResponseDTO;
import com.techpixe.ehr.repo.UthistaUsersRepository;
import com.techpixe.ehr.sentity.UthistaUsers;
import com.techpixe.ehr.service.UthistaUsersService;

@Service
public class UthistaUsersServiceImpl implements UthistaUsersService {

	@Autowired
	private UthistaUsersRepository uthistaUsersRepository;

	@Override
	public ResponseEntity<?> loginByEmail(String email, String password) throws BadRequestException {

		if (email != null && password != null) {
			UthistaUsers user = uthistaUsersRepository.findByEmail(email);
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

			if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {

				String jwt = null;
				LoginResponseDTO response = new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt, user.getId(),
						user.getFullName(), user.getRole(), user.getId());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {

				throw new BadRequestException("invalid email or password");
			}

		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("please enter  email and  password");
			return ResponseEntity.badRequest().body(errorResponse);

		}

	}

}
