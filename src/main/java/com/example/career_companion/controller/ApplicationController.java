package com.example.career_companion.controller;

import com.example.career_companion.dto.ApplicationRequest;
import com.example.career_companion.dto.ApplicationResponse;
import com.example.career_companion.entity.ApplicationStatus;
import com.example.career_companion.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Applications", description = "Job application management endpoints")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @Operation(summary = "Submit a new job application")
    public ResponseEntity<ApplicationResponse> apply(@Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.apply(request));
    }

    @PostMapping("/candidates/{candidateId}")
    @Operation(summary = "Submit job application for candidate ID")
    public ResponseEntity<ApplicationResponse> applyForCandidate(
            @PathVariable Long candidateId,
            @Valid @RequestBody ApplicationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.applyForJob(candidateId, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application details by ID")
    public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/candidate/{candidateId}")
    @Operation(summary = "Get all applications submitted by a candidate")
    public ResponseEntity<List<ApplicationResponse>> getCandidateApplications(@PathVariable Long candidateId) {
        return ResponseEntity.ok(applicationService.getApplicationsByCandidate(candidateId));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "Get all applications submitted for a job")
    public ResponseEntity<List<ApplicationResponse>> getJobApplications(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update job application status and recruiter remarks")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            @RequestParam(required = false) String remarks
    ) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status, remarks));
    }
}