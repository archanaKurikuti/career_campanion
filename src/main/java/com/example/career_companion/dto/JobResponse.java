package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobResponse {

    private Long id;

    private String title;

    private String description;

    private Double salary;

    private Integer experienceRequired;

    private String location;

    private String employmentType;

    private String jobType;

    private Integer vacancies;

    private String companyName;

    private String companyLogoUrl;

    private LocalDate postedDate;

    private LocalDate deadline;

    private String status;
}