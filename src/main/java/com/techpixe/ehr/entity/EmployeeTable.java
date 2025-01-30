package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
//@Table(name = "employee_table")
public class EmployeeTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String empCode;
	private Date dob;
	private String gender;
	private String maritalStatus;
	private String nationality;
	private String address;
	private String city;
	private String state;
	private String country;
	private String identification;
	private String idNumber;
	private String employeeType;
	private LocalDate joiningDate;
	private String bloodGroup;
	private String designation;
	private String department;
	private Long panNo;
	private String bankName;
	private Long bankAccountNo;
	private String iFSCCode;
	private String pfAccountNo;
	private int totalDays;
	private int presentDays;
	private boolean active;
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] photograph;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_Id")
	@JsonBackReference(value = "user-employee")
	private HR user;

	@ManyToOne
	@JsonBackReference(value = "clients-employee")
	@JoinColumn(name = "clients_id")
	private Clients clients;

	@JsonIgnore
	@JsonManagedReference(value = "employee-payhead")
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "employeeTable",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AddPayHeadsToEmployee> addPayHeadsToEmployee = new ArrayList<>();

	@JsonIgnore
	@JsonManagedReference(value = "employee-attendance")
	@OneToMany(mappedBy = "employeeTable", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attendance> attendance = new ArrayList<>();

	@JsonIgnore
	@JsonManagedReference(value = "employee-leave")
	@OneToMany(mappedBy = "employeeTable", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LeaveApprovalTable> leaveapprovalTable = new ArrayList<>();

	@JsonIgnore
	@JsonManagedReference(value = "employee-notification")
	@OneToMany(mappedBy = "employeeTable", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notification = new ArrayList<>();

}
