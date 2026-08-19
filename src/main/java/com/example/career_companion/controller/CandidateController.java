package com.example.career_companion.controller;

import com.example.career_companion.dto.CandidateResponse;
import com.example.career_companion.dto.CandidateUpdateRequest;
import com.example.career_companion.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;


    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponse> getCandidate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                candidateService.getCandidateById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponse> updateCandidate(
            @PathVariable Long id,
            @RequestBody CandidateUpdateRequest request) {

        return ResponseEntity.ok(
                candidateService.updateCandidate(id, request)
        );
    }
}