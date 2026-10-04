package com.example.career_companion.controller;

import com.example.career_companion.dto.AICareerAdviceRequest;
import com.example.career_companion.dto.AIJobMatchRequest;
import com.example.career_companion.dto.AIJobMatchResponse;
import com.example.career_companion.dto.AIResumeAnalysisRequest;
import com.example.career_companion.dto.AIResumeAnalysisResponse;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Job;
import com.example.career_companion.entity.Resume;
import com.example.career_companion.exception.ResourceNotFoundException;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.JobRepository;
import com.example.career_companion.service.ai.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Transactional(readOnly = true)
@Tag(name = "AI Features", description = "AI-powered resume analysis, job matching, recommendations, and career advice endpoints")
public class AIController {

    private final AIService aiService;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;

    public AIController(
            AIService aiService,
            CandidateRepository candidateRepository,
            JobRepository jobRepository
    ) {
        this.aiService = aiService;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/resume-analysis")
    @Operation(summary = "Analyze resume using resumeId or candidate details")
    public ResponseEntity<AIResumeAnalysisResponse> analyzeResumeBody(@Valid @RequestBody AIResumeAnalysisRequest request) {
        if (request.getResumeId() != null) {
            return ResponseEntity.ok(aiService.analyzeResume(request.getResumeId()));
        }
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + request.getCandidateId()));
        String content = candidate.getResume() != null ? candidate.getResume().getContent() : "";
        return ResponseEntity.ok(aiService.analyzeResume(content, candidate));
    }

    @PostMapping("/analyze-resume/{candidateId}")
    @Operation(summary = "Perform AI analysis on candidate resume")
    public ResponseEntity<AIResumeAnalysisResponse> analyzeResume(@PathVariable Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        String content = candidate.getResume() != null ? candidate.getResume().getContent() : "";
        AIResumeAnalysisResponse response = aiService.analyzeResume(content, candidate);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/job-match")
    @Operation(summary = "Calculate AI match score for a job posting")
    public ResponseEntity<AIJobMatchResponse> matchJobBody(@Valid @RequestBody AIJobMatchRequest request) {
        if (request.getCandidateId() != null && request.getJobId() != null) {
            return ResponseEntity.ok(aiService.matchJob(request.getCandidateId(), request.getJobId()));
        }
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + request.getJobId()));
        return ResponseEntity.ok(aiService.matchJob(null, null, job));
    }

    @GetMapping("/match")
    @Operation(summary = "Calculate AI match score between candidate profile and a specific job")
    public ResponseEntity<AIJobMatchResponse> matchJob(
            @RequestParam Long candidateId,
            @RequestParam Long jobId
    ) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        Resume resume = candidate.getResume();
        AIJobMatchResponse match = aiService.matchJob(candidate, resume, job);
        return ResponseEntity.ok(match);
    }

    @GetMapping("/recommendations/{candidateId}")
    @Operation(summary = "Get top AI-matched job recommendations for candidate")
    public ResponseEntity<List<AIJobMatchResponse>> getRecommendations(@PathVariable Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        List<Job> activeJobs = jobRepository.findByIsActiveTrue();
        Resume resume = candidate.getResume();
        List<AIJobMatchResponse> recommendations = aiService.recommendJobs(candidate, resume, activeJobs);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/career-advice")
    @Operation(summary = "Get AI career advice and guidance")
    public ResponseEntity<Map<String, String>> getCareerAdvice(@Valid @RequestBody AICareerAdviceRequest request) {
        Candidate candidate = null;
        Resume resume = null;

        if (request.getCandidateId() != null) {
            candidate = candidateRepository.findById(request.getCandidateId()).orElse(null);
            if (candidate != null) {
                resume = candidate.getResume();
            }
        }

        String advice = aiService.getCareerAdvice(request.getQuestion(), candidate, resume);

        Map<String, String> response = new HashMap<>();
        response.put("advice", advice);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/advice")
    @Operation(summary = "Backward-compatible alias for career advice")
    public ResponseEntity<Map<String, String>> getCareerAdviceAlias(@Valid @RequestBody AICareerAdviceRequest request) {
        return getCareerAdvice(request);
    }
}

