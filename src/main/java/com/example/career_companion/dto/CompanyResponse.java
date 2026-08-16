package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyResponse {

    private Long id;

    private String companyName;

    private String description;

    private String website;

    private String logoUrl;

    private String industry;

    private String location;

    private Integer employeeCount;

    private String email;

    private String phone;

    private Boolean verified;
}