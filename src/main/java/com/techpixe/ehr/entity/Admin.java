package com.techpixe.ehr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_Id")
    private Long admin_Id;

    @Column(name = "fullName", nullable = false)
    @Pattern(regexp = "^[A-Za-z]+$", message = "Full name can only contain alphabets")
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Please provide a valid email address")
    @Pattern(regexp = ".+@.+\\..+", message = "Email address must contain @ symbol")

    private String email;

    @Column(name = "mobileNumber", unique = true, nullable = false)
    @NotNull(message = "Mobile number is required")
    @Digits(integer = 15, fraction = 0, message = "Mobile number should only contain numeric digits")
    private Long mobileNumber;

    @Column(name = "password", nullable = false)
    private String password;

    private String role;

}
