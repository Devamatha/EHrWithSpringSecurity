package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscription_Id;
    private String planType; // Quarterly, Monthly, Yearly
    private double amount;
    @Column(length = 1000)
    private String discription;
    @Column(length = 1000)
    private String additionalFeatures;
    private Long totalResumes;
    //private Long totalImages;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_Id")
    private Admin admin;


}










