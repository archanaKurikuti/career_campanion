package com.example.career_companion.controller;

import com.example.career_companion.dto.ResumeResponse;
import com.example.career_companion.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resumes")
@Tag(name = "Resumes", description = "Resume upload, download, and AI analysis endpoints")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/upload/{candidateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload candidate resume file and trigger AI analysis")
    public ResponseEntity<ResumeResponse> uploadResume(
            @PathVariable Long candidateId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.uploadResume(candidateId, file));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resume metadata and AI analysis by resume ID")
    public ResponseEntity<ResumeResponse> getResumeById(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.getResumeById(id));
    }

    @GetMapping("/candidate/{candidateId}")
    @Operation(summary = "Get candidate resume details and AI analysis")
    public ResponseEntity<ResumeResponse> getResumeByCandidateId(@PathVariable Long candidateId) {
        return ResponseEntity.ok(resumeService.getResumeByCandidateId(candidateId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete resume")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }
}