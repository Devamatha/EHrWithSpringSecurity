package com.techpixe.ehr.sentity;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "site_users")

public class Site_users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long employeeId;
	private String employee_name;
	private String employee_designation;
	@Column(columnDefinition = "TIME")
	private Time clock_in;
	@Column(columnDefinition = "TIME")
	private Time clock_out;

	private String latittude;
	private String longitude;
	private String duration;

	private LocalDate date;
	private LocalDateTime createdAt;
	private String checkIn_address;
	private String checkOut_address;

	private boolean isAttendance;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] clock_out_employee_img;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] clock_in_employee_img;

}
