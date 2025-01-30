package com.techpixe.ehr.sentity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Table(name = "uthista_users")

public class UthistaUsers {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String fullName;
	@Column(nullable = false, unique = true)
	@Email(message = "Invalid email")

	private String email;
	@Column(nullable = false, unique = true)
	private Long mobileNumber;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private LocalDate createdAt;
	private String role;

	
}
