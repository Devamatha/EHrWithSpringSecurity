package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
//@Table(name = "leave_approval_table")
public class LeaveApprovalTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String subject;
    private LocalDate startDate;
    private LocalDate endDate;
    private String message;
    private String type;
    private String status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "employee-leave")
    @JoinColumn(name = "empId")
    private EmployeeTable employeeTable;
}
