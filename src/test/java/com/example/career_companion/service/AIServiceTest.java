package com.example.career_companion.service;

import com.example.career_companion.dto.AIJobMatchResponse;
import com.example.career_companion.dto.AIResumeAnalysisResponse;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Job;
import com.example.career_companion.entity.Skill;
import com.example.career_companion.service.ai.AIServiceImpl;
import com.example.career_companion.service.ai.GeminiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AIServiceTest {

    private AIServiceImpl aiService;
    private GeminiService geminiService;

    @BeforeEach
    void setUp() {
        geminiService = mock(GeminiService.class);
        aiService = new AIServiceImpl(geminiService);
    }

    @Test
    void analyzeResume_ExtractsSkillsAndCalculatesScore() {
        Candidate candidate = new Candidate();
        candidate.setName("Jane Developer");
        candidate.setExperience(3);

        Skill java = new Skill();
        java.setName("Java");
        Skill spring = new Skill();
        spring.setName("Spring Boot");
        candidate.setSkills(List.of(java, spring));

        String content = "Experienced in Java, Spring Boot, MySQL, and building RESTful Microservices.";

        AIResumeAnalysisResponse response = aiService.analyzeResume(content, candidate);

        assertNotNull(response);
        assertTrue(response.getResumeQualityScore() >= 60);
        assertTrue(response.getDetectedSkills().contains("Java"));
        assertTrue(response.getDetectedSkills().contains("Spring Boot"));
    }

    @Test
    void matchJob_CalculatesMatchScoreCorrectly() {
        Candidate candidate = new Candidate();
        Skill java = new Skill();
        java.setName("Java");
        candidate.setSkills(List.of(java));
        candidate.setExperience(2);

        Job job = new Job();
        job.setId(1L);
        job.setTitle("Java Developer");
        job.setDescription("Looking for a Java developer with Spring experience.");
        job.setExperienceRequired(2);

        AIJobMatchResponse match = aiService.matchJob(candidate, null, job);

        assertNotNull(match);
        assertTrue(match.getMatchScore() > 50.0);
        assertEquals("Java Developer", match.getJobTitle());
    }

    @Test
    void getCareerAdvice_ReturnsRelevantAdvice() {
        Candidate candidate = new Candidate();
        candidate.setName("Alex");

        when(geminiService.generateAdvice(anyString()))
                .thenReturn("Focus on Microservices and Backend architecture.");

        String advice = aiService.getCareerAdvice("How can I become a better backend developer?", candidate, null);

        assertNotNull(advice);
        assertTrue(advice.contains("Microservices") || advice.contains("Backend"));
    }
}
