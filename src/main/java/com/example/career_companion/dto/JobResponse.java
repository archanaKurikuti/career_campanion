package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String responsibilities;
    private Double salary;
    private Integer experienceRequired;
    private String location;
    private String employmentType;
    private String jobType;
    private Integer vacancies;
    private Long companyId;
    private String companyName;
    private String companyLogoUrl;
    private Long recruiterId;
    private String recruiterName;
    private LocalDateTime postedDate;
    private LocalDateTime deadline;
    private Boolean isActive;
    private String status;
}