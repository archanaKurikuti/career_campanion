package com.example.career_companion.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResumeResponse {

    private Long id;

    private String fileName;

    private String resumeUrl;

    private Long fileSize;

    private LocalDateTime uploadedAt;
}