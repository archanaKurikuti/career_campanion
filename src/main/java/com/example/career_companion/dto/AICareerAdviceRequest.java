package com.example.career_companion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AICareerAdviceRequest {

    @NotBlank(message = "Question is required")
    private String question;

    private Long candidateId;
}
