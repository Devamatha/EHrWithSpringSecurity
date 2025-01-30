package com.techpixe.ehr.controller;

import com.techpixe.ehr.constant.ApplicationConstants;
import com.techpixe.ehr.dto.LoginRequestDTO;
import com.techpixe.ehr.dto.LoginResponseDTO;
import com.techpixe.ehr.dto.RegisterDto;
import com.techpixe.ehr.entity.Clients;
import com.techpixe.ehr.repository.ClientsRepository;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.ClientsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor

public class ClientsController {
	private final Environment env;
	private final AuthenticationManager authenticationManager;
	@Autowired
	public ClientsService clientsService;

	@Autowired
	public ClientsRepository clientsRepository;

	@Autowired
	public EmployeeTableRepository employeeTableRepository;

	@Autowired
	public UserRepository userRepository;

	@PostMapping("/save/Employee/{id}")
	public ResponseEntity<Map<String, Object>> saveEmployee(@RequestBody RegisterDto registerDto,
			@PathVariable Long id) {
		clientsService.registerEmployee(registerDto, id);
		return ResponseEntity.ok(Collections.singletonMap("message", " Employee Data saved successfully"));
	}

	@PostMapping("/save")
	public ResponseEntity<Clients> saveHRAndAdmin(@RequestParam String fullName, @RequestParam String email,
			@RequestParam Long mobileNumber, @RequestParam String role,
			@RequestParam(required = false) MultipartFile logo, @RequestParam(required = false) String companyName,
			@RequestParam(required = false) String authorizedCompanyName,
			@RequestParam(required = false) String address) {
		Clients register = clientsService.registerHRAndAdmin(fullName, email, mobileNumber, role, logo, companyName,
				authorizedCompanyName, address);
		return ResponseEntity.ok(register);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> apiLogin(@RequestBody LoginRequestDTO loginRequest) {
		String jwt = "";
		Long id = 0L;
		String fullName = "";
		String role = "";
		Long clientId = 0L;
		Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),
				loginRequest.password());

		Authentication authenticationResponse = authenticationManager.authenticate(authentication);
		List<Object[]> result = clientsRepository.findIdAndFullNameByEmail(authenticationResponse.getName());
		for (Object[] row : result) {
			id = (Long) row[0];
			fullName = (String) row[1];
			clientId = (Long) row[0];

		}

		role = authenticationResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		if (role.equals("ROLE_EMPLOYEE")) {
			List<Long> dataId = employeeTableRepository.findEmployeeIdsByClientId(id);
			id = dataId.get(0);
		}
		if (role.equals("ROLE_HR")) {
			List<Long> dataId = userRepository.findHRIdsByClientId(id);
			id = dataId.get(0);

		}

		if (null != authenticationResponse && authenticationResponse.isAuthenticated()) {
			if (null != env) {
				String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
						ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
				SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
				jwt = Jwts.builder().issuer("EHr_Application").subject("JWT Token")
						.claim("username", authenticationResponse.getName())
						.claim("authorities",
								authenticationResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority)
										.collect(Collectors.joining(",")))
						.issuedAt(new java.util.Date())
						.expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000)).signWith(secretKey)
						.compact();

			} else {
				System.out.println(env + "is not found");
			}

		} else {
			System.out.println(authenticationResponse + "is not found");
		}

		return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER, jwt)
				.body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt, id, fullName, role, clientId));
	}

	@PostMapping("/changepassword/{Id}")
	public ResponseEntity<?> changePassword(@PathVariable Long Id, @RequestParam String password,
			@RequestParam String confirmPassword) {
		if (password != null && confirmPassword != null) {
			return clientsService.changePassword(Id, password, confirmPassword);
		} else {

			return ResponseEntity.internalServerError()
					.body(Collections.singletonMap("error", "Password is not present"));
		}
	}

	private boolean isEmail(String emailOrMobileNumber) {
		return emailOrMobileNumber.contains("@");
	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestParam String email) {
		if (email != null) {
			if (isEmail(email)) {
				return clientsService.forgotPassword(email);
			} else {

				return ResponseEntity.internalServerError()
						.body(Collections.singletonMap("error", "Invalid Email Pattern."));
			}
		} else {

			return ResponseEntity.internalServerError().body(Collections.singletonMap("error", "Email is not present"));
		}
	}
}
