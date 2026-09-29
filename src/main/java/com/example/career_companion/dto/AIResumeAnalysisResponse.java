package com.example.career_companion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AIResumeAnalysisResponse {
    private List<String> detectedSkills;
    private String experienceSummary;
    private String educationSummary;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> missingSkills;
    private Integer resumeQualityScore;
    private List<String> suggestedImprovements;
    private List<String> recommendedRoles;
}
