package com.techpixe.ehr.controller;

import com.techpixe.ehr.constant.ApplicationConstants;
import com.techpixe.ehr.dto.LoginRequestDTO;
import com.techpixe.ehr.dto.LoginResponseDTO;
import com.techpixe.ehr.dto.RegisterDto;
import com.techpixe.ehr.entity.Clients;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor

public class ClientsController {
    private final Environment env;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public ClientsService clientsService;

    @PostMapping("/save/Employee/{id}")
    public ResponseEntity<?> saveEmployee(@RequestBody RegisterDto registerDto ,@PathVariable(required = false) Long id) {
        clientsService.registerEmployee(registerDto,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerDto);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveClient(@RequestBody RegisterDto registerDto) {
        clientsService.registerClients(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> apiLogin(@RequestBody LoginRequestDTO loginRequest) {
        String jwt = "";
        System.out.println(loginRequest.username());
        System.out.println(loginRequest.password());
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),
                loginRequest.password());

        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        if (null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != env) {
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder().issuer("Eazy Bank").subject("JWT Token")
                        .claim("username", authenticationResponse.getName())
                        .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .issuedAt(new java.util.Date())
                        .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                        .signWith(secretKey).compact();
            } else {
                System.out.println(env + "is not found");
            }

        } else {
            System.out.println(authenticationResponse + "is not found");
        }

        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER, jwt)
                .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }
}
