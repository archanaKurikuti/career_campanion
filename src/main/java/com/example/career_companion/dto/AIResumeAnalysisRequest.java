package com.example.career_companion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResumeAnalysisRequest {

    @NotNull(message = "Resume ID is required")
    private Long resumeId;

    private Long candidateId;
}
