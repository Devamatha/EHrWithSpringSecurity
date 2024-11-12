package com.techpixe.ehr.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class RegisterDto {
    //ClientTable
    @JsonIgnore
    private Long id;
    private String fullName;
    private String email;
    private Long mobileNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate createdAt;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String role;
    //HR Table
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String companyName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String authorizedCompanyName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String address;

    //Employee Table
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String empCode;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date dob;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String gender;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String maritalStatus;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String nationality;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String city;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String state;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String country;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String emailId;
   // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  //  private Long contactNo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String identification;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String idNumber;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String employeeType;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate joiningDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String bloodGroup;


}
