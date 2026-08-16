package com.example.career_companion.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateUpdateRequest {

    private String education;

    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience;

    private String currentCompany;

    private String location;

    private String bio;

    private String github;

    private String linkedin;

    private String portfolio;

    private String expectedSalary;

    private Boolean openToWork;
}