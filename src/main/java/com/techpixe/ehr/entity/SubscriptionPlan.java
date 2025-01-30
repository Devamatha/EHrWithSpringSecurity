package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
////@Table(name = "subscription_plan")
public class SubscriptionPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subscription_Id;

	@Column(nullable = false)
	private String planType; // Quarterly, Monthly, Yearly

	@Column(nullable = false)
	private double amount;

	@Column(nullable = true)
	private LocalDate startDate;

	@Column(nullable = true)
	private LocalDate endDate;

	@Column(length = 1000, nullable = true)
	private String description;

	@Column(length = 1000, nullable = true)
	private String additionalFeatures;

	private long totalResumes;

	private LocalDate createdAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_Id")
	@JsonBackReference
	private HR user;

}
