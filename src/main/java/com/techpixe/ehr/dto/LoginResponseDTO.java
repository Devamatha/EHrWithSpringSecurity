package com.techpixe.ehr.dto;

public record LoginResponseDTO(String status, String jwtToken,Long id,String fullName,String role,Long clientId) {
}

