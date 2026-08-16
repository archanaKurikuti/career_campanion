package com.example.career_companion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String description;

    private String website;

    private String logoUrl;

    private String industry;

    private String location;

    private Integer employeeCount;

    @Email(message = "Invalid email")
    private String email;

    private String phone;
}