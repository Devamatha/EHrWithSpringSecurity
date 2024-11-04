package com.techpixe.ehr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private long id;
    private String fullName;
    private String email;
    private Long mobileNumber;
    private String role;

}
