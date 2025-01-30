package com.techpixe.ehr.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "clients")
public class Clients {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String fullName;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false, unique = true)
	private Long mobileNumber;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private LocalDate createdAt;
	private String role;

	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "clients", fetch = FetchType.EAGER)
	private List<HR> hrList = new ArrayList<>();

	@JsonIgnore
	@JsonManagedReference(value = "clients-employee")
	@OneToMany(mappedBy = "clients", fetch = FetchType.EAGER)
	private List<EmployeeTable> employeeTableList = new ArrayList<>();

	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "clients", fetch = FetchType.EAGER)
	private List<Plan> plans = new ArrayList<>();
}
