package com.example.career_companion.service;

import com.example.career_companion.dto.ApplicationRequest;
import com.example.career_companion.dto.ApplicationResponse;
import com.example.career_companion.entity.Application;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Job;
import com.example.career_companion.entity.ApplicationStatus;
import com.example.career_companion.repository.ApplicationRepository;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private JobRepository jobRepository;

    // APPLY FOR A JOB
    public ApplicationResponse applyForJob(
            Long candidateId,
            ApplicationRequest request) {

        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        Job job = jobRepository
                .findById(request.getJobId())
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        // Check duplicate application
        boolean alreadyApplied =
                applicationRepository
                        .existsByCandidateIdAndJobId(
                                candidateId,
                                request.getJobId()
                        );

        if (alreadyApplied) {
            throw new RuntimeException(
                    "Candidate has already applied for this job"
            );
        }

        Application application = new Application();

        application.setCandidate(candidate);
        application.setJob(job);
        application.setCoverLetter(request.getCoverLetter());

        application.setStatus(
                ApplicationStatus.APPLIED
        );

        Application saved =
                applicationRepository.save(application);

        return mapToResponse(saved);
    }

        public ApplicationResponse apply(ApplicationRequest request) {
                Long candidateId = (request.getCandidateId() != null) ? request.getCandidateId() : 1L;
                return applyForJob(candidateId, request);
        }

    // GET APPLICATION BY ID
    public ApplicationResponse getApplicationById(Long id) {

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        return mapToResponse(application);
    }

    // GET APPLICATIONS OF A CANDIDATE
    public List<ApplicationResponse> getApplicationsByCandidate(
            Long candidateId) {

        if (!candidateRepository.existsById(candidateId)) {
            throw new RuntimeException(
                    "Candidate not found");
        }

        return applicationRepository
                .findByCandidateId(candidateId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET APPLICATIONS FOR A JOB
    public List<ApplicationResponse> getApplicationsByJob(
            Long jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException(
                    "Job not found");
        }

        return applicationRepository
                .findByJobId(jobId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // UPDATE APPLICATION STATUS
    public ApplicationResponse updateStatus(
            Long id,
            ApplicationStatus status) {

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        application.setStatus(status);

        Application updated =
                applicationRepository.save(application);

        return mapToResponse(updated);
    }

    // ENTITY → RESPONSE DTO
    private ApplicationResponse mapToResponse(
            Application application) {

        ApplicationResponse response =
                new ApplicationResponse();

        response.setId(application.getId());

        if (application.getCandidate() != null) {

            response.setCandidateId(
                    application.getCandidate().getId()
            );

            response.setCandidateName(
                    application.getCandidate().getName()
            );
        }

        if (application.getJob() != null) {

            response.setJobId(
                    application.getJob().getId()
            );

            response.setJobTitle(
                    application.getJob().getTitle()
            );

            if (application.getJob().getCompany() != null) {

                response.setCompanyName(
                        application.getJob()
                                .getCompany()
                                .getCompanyName()
                );
            }
        }

        response.setStatus(application.getStatus() != null ? application.getStatus().name() : null);
        response.setCoverLetter(application.getCoverLetter());
        response.setAiMatchScore(
                application.getAiMatchScore()
        );
        response.setRecruiterRemarks(
                application.getRecruiterRemarks()
        );
        response.setAppliedAt(application.getAppliedAt());

        return response;
    }
}