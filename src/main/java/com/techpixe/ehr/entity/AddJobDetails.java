package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "add_job_details")
public class AddJobDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long jobId;

	@Column(nullable = false)
	private String jobTitle;

	// private List<String> jobkeyskills;

	private LocalDate createdAt;

	@Column(nullable = false)
	private int yearsOfExperience;

	@Column(nullable = false)
	private int noOfVacancies;

	@Column(nullable = false)
	private String overallPercentage;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_Id")
	@JsonBackReference
	private HR user;

}
