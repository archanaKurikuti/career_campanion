package com.example.career_companion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @Positive(message = "Salary must be positive")
    private Double salary;

    private Integer experienceRequired;

    private String location;

    private String employmentType;

    private String jobType;

    private Integer vacancies;

    private LocalDate deadline;
}