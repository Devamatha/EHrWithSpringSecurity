package com.techpixe.ehr.dto;

import com.techpixe.ehr.entity.AddPayHeadsToEmployee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePayHeadDTO {
    private String employeeFullName;
    private String companyName;
    private String authorizedCompanyName;
    private String address;
    private byte[] logo;
    private List<AddPayHeadsToEmployee> payHeads;
    // Add other fields as necessary
}


