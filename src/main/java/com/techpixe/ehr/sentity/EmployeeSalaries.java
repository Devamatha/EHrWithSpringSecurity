package com.techpixe.ehr.sentity;

import java.time.LocalDateTime;

import com.techpixe.ehr.enumClass.CategoryType;
import com.techpixe.ehr.enumClass.FormType;
import com.techpixe.ehr.enumClass.PaymentMode;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "employee_salaries")

public class EmployeeSalaries {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String empId;
	private String empName;

	private long salaryAmount;
	@Enumerated(EnumType.STRING)
	private PaymentMode paymentMode;
	private String remarks;
	@Enumerated(EnumType.STRING)
	private CategoryType categoryType;
	@Enumerated(EnumType.STRING)
	private FormType formType;
	private String transactionId;
	private String email;
	
	private LocalDateTime createdAt;

	
}
