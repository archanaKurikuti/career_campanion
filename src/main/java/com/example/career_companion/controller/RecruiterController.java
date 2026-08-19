package com.example.career_companion.controller;

import com.example.career_companion.dto.recruiter.RecruiterResponse;
import com.example.career_companion.dto.recruiter.RecruiterUpdateRequest;
import com.example.career_companion.service.RecruiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;


    @GetMapping("/{id}")
    public ResponseEntity<RecruiterResponse> getRecruiter(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                recruiterService.getRecruiterById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruiterResponse> updateRecruiter(
            @PathVariable Long id,
            @RequestBody RecruiterUpdateRequest request) {

        return ResponseEntity.ok(
                recruiterService.updateRecruiter(id, request)
        );
    }
}