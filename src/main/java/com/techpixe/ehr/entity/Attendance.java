package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
//@Table(name = "attendance")
public class Attendance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;
	private String name;
	private String empCode;
	private LocalTime punchIn;
	private LocalTime punchOut;
	private String punchInMessage;
	private String punchOutMessage;
	private String workingHours;
	private boolean isAttendance;
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference(value = "employee-attendance")
	@JoinColumn(name = "emp_Id")
	private EmployeeTable employeeTable;

}
