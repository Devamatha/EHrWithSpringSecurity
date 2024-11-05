package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.techpixe.ehr.entity.EmployeeTable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonManagedReference
    @OneToMany(mappedBy = "clients", fetch = FetchType.EAGER)
    private List<EmployeeTable> employeeTableList = new ArrayList<>();

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "clients", fetch = FetchType.EAGER)
    private List<Plan> plans = new ArrayList<>();
}
