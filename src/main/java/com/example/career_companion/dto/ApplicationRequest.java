package com.example.career_companion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private String coverLetter;
}