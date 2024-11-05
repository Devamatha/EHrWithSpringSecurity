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
    @JsonBackReference
    private HR user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "clients_id")
    private Clients clients;

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employeeTable")
    private List<AddPayHeadsToEmployee> addPayHeadsToEmployee = new ArrayList<>();
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "employeeTable", fetch = FetchType.EAGER)
    private List<Attendance> attendance = new ArrayList<>();
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "employeeTable", fetch = FetchType.EAGER)
    private List<LeaveApprovalTable> leaveapprovalTable = new ArrayList<>();


}
