package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
//@Table(name = "add_pay_heads_to_employee")
public class AddPayHeadsToEmployee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String selectedPayHead;
	private String selectedPayHeadType;
	private Double payHeadAmount;
	private String empCode;
	private String empName;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "empId")
	@JsonBackReference(value = "employee-payhead")
	private EmployeeTable employeeTable;

}
