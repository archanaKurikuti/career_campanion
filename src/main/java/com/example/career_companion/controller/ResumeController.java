// ...existing code...
package com.example.career_companion.controller;

import com.example.career_companion.dto.ResumeResponse;
import com.example.career_companion.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                resumeService.uploadResume(file)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResume(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                resumeService.getResumeById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable Long id) {

        resumeService.deleteResume(id);

        return ResponseEntity.noContent().build();
    }
}
// ...existing code...