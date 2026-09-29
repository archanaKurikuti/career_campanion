package com.example.career_companion.service;

import com.example.career_companion.dto.ApplicationRequest;
import com.example.career_companion.dto.ApplicationResponse;
import com.example.career_companion.dto.AIJobMatchResponse;
import com.example.career_companion.entity.Application;
import com.example.career_companion.entity.ApplicationStatus;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Job;
import com.example.career_companion.entity.NotificationType;
import com.example.career_companion.exception.BadRequestException;
import com.example.career_companion.exception.DuplicateResourceException;
import com.example.career_companion.exception.ResourceNotFoundException;
import com.example.career_companion.repository.ApplicationRepository;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.JobRepository;
import com.example.career_companion.service.ai.AIService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final NotificationService notificationService;
    private final AIService aiService;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            CandidateRepository candidateRepository,
            JobRepository jobRepository,
            NotificationService notificationService,
            AIService aiService
    ) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.notificationService = notificationService;
        this.aiService = aiService;
    }

    public ApplicationResponse applyForJob(Long candidateId, ApplicationRequest request) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + request.getJobId()));

        if (job.getDeadline() != null && LocalDateTime.now().isAfter(job.getDeadline())) {
            throw new BadRequestException("Application deadline for this job has passed.");
        }

        boolean alreadyApplied = applicationRepository.existsByCandidateIdAndJobId(candidateId, request.getJobId());
        if (alreadyApplied) {
            throw new DuplicateResourceException("You have already applied for this job.");
        }

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJob(job);
        application.setCoverLetter(request.getCoverLetter());
        application.setStatus(ApplicationStatus.APPLIED);

        // Calculate initial AI match score
        try {
            AIJobMatchResponse match = aiService.matchJob(candidate, candidate.getResume(), job);
            application.setAiMatchScore(match.getMatchScore());
        } catch (Exception e) {
            application.setAiMatchScore(75.0);
        }

        Application saved = applicationRepository.save(application);

        // Send Notification to Recruiter if assigned
        if (job.getRecruiter() != null) {
            notificationService.createNotification(
                    job.getRecruiter(),
                    "New Job Application Received",
                    candidate.getName() + " applied for " + job.getTitle(),
                    NotificationType.APPLICATION_STATUS
            );
        }

        return mapToResponse(saved);
    }

    public ApplicationResponse apply(ApplicationRequest request) {
        Long candidateId = (request.getCandidateId() != null) ? request.getCandidateId() : 1L;
        return applyForJob(candidateId, request);
    }

    public ApplicationResponse getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
        return mapToResponse(application);
    }

    public List<ApplicationResponse> getApplicationsByCandidate(Long candidateId) {
        if (!candidateRepository.existsById(candidateId)) {
            throw new ResourceNotFoundException("Candidate not found with id: " + candidateId);
        }

        return applicationRepository.findByCandidateId(candidateId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ApplicationResponse> getApplicationsByJob(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }

        return applicationRepository.findByJobId(jobId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ApplicationResponse updateStatus(Long id, ApplicationStatus status, String remarks) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        application.setStatus(status);
        if (remarks != null && !remarks.trim().isEmpty()) {
            application.setRecruiterRemarks(remarks);
        }

        Application updated = applicationRepository.save(application);

        // Send Notification to Candidate
        if (application.getCandidate() != null) {
            String title = "Application Update: " + application.getJob().getTitle();
            String msg = "Your application status has been updated to: " + status.name()
                    + (remarks != null ? ". Remarks: " + remarks : "");
            notificationService.createNotification(
                    application.getCandidate(),
                    title,
                    msg,
                    NotificationType.APPLICATION_STATUS
            );
        }

        return mapToResponse(updated);
    }

    public ApplicationResponse mapToResponse(Application application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());

        if (application.getCandidate() != null) {
            response.setCandidateId(application.getCandidate().getId());
            response.setCandidateName(application.getCandidate().getName());
        }

        if (application.getJob() != null) {
            response.setJobId(application.getJob().getId());
            response.setJobTitle(application.getJob().getTitle());
            if (application.getJob().getCompany() != null) {
                response.setCompanyName(application.getJob().getCompany().getCompanyName());
            }
        }

        response.setStatus(application.getStatus() != null ? application.getStatus().name() : null);
        response.setCoverLetter(application.getCoverLetter());
        response.setAiMatchScore(application.getAiMatchScore());
        response.setRecruiterRemarks(application.getRecruiterRemarks());
        response.setAppliedAt(application.getAppliedAt());

        return response;
    }
}