package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationResponse {

    private Long id;

    private Long candidateId;

    private String candidateName;

    private Long jobId;

    private String jobTitle;

    private String companyName;

    private String status;

    private String coverLetter;

    private Double aiMatchScore;

    private String recruiterRemarks;

    private LocalDateTime appliedAt;
}