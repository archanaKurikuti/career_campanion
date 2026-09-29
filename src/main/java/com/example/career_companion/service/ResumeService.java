package com.example.career_companion.service;

import com.example.career_companion.dto.AIResumeAnalysisResponse;
import com.example.career_companion.dto.ResumeResponse;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Resume;
import com.example.career_companion.exception.BadRequestException;
import com.example.career_companion.exception.ResourceNotFoundException;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.ResumeRepository;
import com.example.career_companion.service.ai.AIService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final CandidateRepository candidateRepository;
    private final AIService aiService;

    private static final String UPLOAD_DIR = "uploads/resumes/";

    public ResumeService(
            ResumeRepository resumeRepository,
            CandidateRepository candidateRepository,
            AIService aiService
    ) {
        this.resumeRepository = resumeRepository;
        this.candidateRepository = candidateRepository;
        this.aiService = aiService;
    }

    public ResumeResponse uploadResume(Long candidateId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File must not be empty");
        }

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalName != null && originalName.contains(".")) {
                fileExtension = originalName.substring(originalName.lastIndexOf("."));
            }

            String storedFileName = UUID.randomUUID().toString() + fileExtension;
            Path path = Paths.get(UPLOAD_DIR + storedFileName);
            Files.write(path, file.getBytes());

            String fileUrl = "/api/resumes/download/" + storedFileName;
            String textContent = extractText(file);

            Resume resume = candidate.getResume();
            if (resume == null) {
                resume = new Resume();
            }

            resume.setFileName(originalName);
            resume.setFileUrl(fileUrl);
            resume.setContent(textContent);

            AIResumeAnalysisResponse aiAnalysis = aiService.analyzeResume(textContent, candidate);
            if (aiAnalysis != null) {
                resume.setSummary("Quality Score: " + aiAnalysis.getResumeQualityScore() + "/100. " + aiAnalysis.getExperienceSummary());
            }

            resume.setCandidate(candidate);
            Resume saved = resumeRepository.save(resume);

            candidate.setResume(saved);
            candidateRepository.save(candidate);

            ResumeResponse response = mapToResponse(saved, file.getSize());
            response.setAiAnalysis(aiAnalysis);
            return response;

        } catch (IOException e) {
            throw new BadRequestException("Failed to upload resume file: " + e.getMessage());
        }
    }

    public ResumeResponse getResumeById(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with id: " + id));

        AIResumeAnalysisResponse aiAnalysis = aiService.analyzeResume(resume.getContent(), resume.getCandidate());
        ResumeResponse response = mapToResponse(resume, null);
        response.setAiAnalysis(aiAnalysis);
        return response;
    }

    public ResumeResponse getResumeByCandidateId(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        if (candidate.getResume() == null) {
            throw new ResourceNotFoundException("Candidate has not uploaded a resume yet.");
        }

        Resume resume = candidate.getResume();
        AIResumeAnalysisResponse aiAnalysis = aiService.analyzeResume(resume.getContent(), candidate);
        ResumeResponse response = mapToResponse(resume, null);
        response.setAiAnalysis(aiAnalysis);
        return response;
    }

    public void deleteResume(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with id: " + id));

        if (resume.getCandidate() != null) {
            resume.getCandidate().setResume(null);
            candidateRepository.save(resume.getCandidate());
        }

        resumeRepository.delete(resume);
    }

    private String extractText(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        byte[] bytes = file.getBytes();
        String raw = new String(bytes);
        if (filename.endsWith(".pdf") || filename.endsWith(".docx")) {
            return raw.replaceAll("[^a-zA-Z0-9\\s\\.\\,\\@\\-\\+]", " ");
        }
        return raw;
    }

    private ResumeResponse mapToResponse(Resume resume, Long fileSize) {
        ResumeResponse resp = new ResumeResponse();
        if (resume != null) {
            resp.setId(resume.getId());
            resp.setFileName(resume.getFileName());
            resp.setResumeUrl(resume.getFileUrl());
            resp.setFileSize(fileSize);
            resp.setContent(resume.getContent());
            resp.setSummary(resume.getSummary());
            resp.setUploadedAt(LocalDateTime.now());
            if (resume.getCandidate() != null) {
                resp.setCandidateId(resume.getCandidate().getId());
            }
        }
        return resp;
    }
}