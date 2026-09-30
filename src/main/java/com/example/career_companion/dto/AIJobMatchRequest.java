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
public class AIJobMatchRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private Long candidateId;
}
