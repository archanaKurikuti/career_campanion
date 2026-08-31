package com.example.career_companion.controller;

import com.example.career_companion.entity.Recruiter;
import com.example.career_companion.service.RecruiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;


    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getRecruiter(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                recruiterService.getRecruiterById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recruiter> updateRecruiter(
            @PathVariable Long id,
            @RequestBody Recruiter request) {

        return ResponseEntity.ok(
                recruiterService.updateRecruiter(id, request)
        );
    }
}