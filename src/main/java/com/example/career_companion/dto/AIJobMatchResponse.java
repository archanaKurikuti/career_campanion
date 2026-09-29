package com.example.career_companion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AIJobMatchResponse {
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String explanation;
}
