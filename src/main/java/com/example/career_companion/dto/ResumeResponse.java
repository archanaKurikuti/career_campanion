package com.example.career_companion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {

    private Long id;
    private String fileName;
    private String resumeUrl;
    private Long fileSize;
    private String content;
    private String summary;
    private Long candidateId;
    private LocalDateTime uploadedAt;
    private AIResumeAnalysisResponse aiAnalysis;
}