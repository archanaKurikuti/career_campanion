package com.example.career_companion.controller;

import com.example.career_companion.dto.RecruiterResponse;
import com.example.career_companion.dto.RecruiterUpdateRequest;
import com.example.career_companion.service.RecruiterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@Tag(name = "Recruiters", description = "Recruiter profile management endpoints")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping
    @Operation(summary = "Get all recruiters")
    public ResponseEntity<List<RecruiterResponse>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterService.getAllRecruiters());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recruiter details by ID")
    public ResponseEntity<RecruiterResponse> getRecruiter(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.getRecruiterById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update recruiter details")
    public ResponseEntity<RecruiterResponse> updateRecruiter(
            @PathVariable Long id,
            @Valid @RequestBody RecruiterUpdateRequest request
    ) {
        return ResponseEntity.ok(recruiterService.updateRecruiter(id, request));
    }
}