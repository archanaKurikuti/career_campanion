package com.example.career_companion.controller;

import com.example.career_companion.dto.JobRequest;
import com.example.career_companion.dto.JobResponse;
import com.example.career_companion.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @RequestBody JobRequest request) {

        return ResponseEntity.ok(
                jobService.createJob(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJob(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                jobService.getJobById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {

        return ResponseEntity.ok(
                jobService.getAllJobs()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestBody JobRequest request) {

        return ResponseEntity.ok(
                jobService.updateJob(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id) {

        jobService.deleteJob(id);

        return ResponseEntity.noContent().build();
    }
}