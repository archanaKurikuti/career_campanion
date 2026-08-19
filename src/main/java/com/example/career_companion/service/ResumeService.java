// ...existing code...
package com.example.career_companion.service;

import com.example.career_companion.dto.ResumeResponse;
import com.example.career_companion.entity.Resume;
import com.example.career_companion.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    public ResumeResponse uploadResume(MultipartFile file) {

        Resume resume = new Resume();

        try {
            resume.setFileName(file.getOriginalFilename());
            resume.setFileType(file.getContentType());
            resume.setData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }

        Resume savedResume = resumeRepository.save(resume);

        return mapToResponse(savedResume);
    }

    // changed to return ResumeResponse (was returning Resume entity)
    public ResumeResponse getResumeById(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        return mapToResponse(resume);
    }

    public void deleteResume(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        resumeRepository.delete(resume);
    }

    // minimal mapper to satisfy compilation; extend mapping as needed
    private ResumeResponse mapToResponse(Resume resume) {
        ResumeResponse resp = new ResumeResponse();
        // map fields as needed, e.g. resp.setId(resume.getId());
        return resp;
    }
}
// ...existing code...