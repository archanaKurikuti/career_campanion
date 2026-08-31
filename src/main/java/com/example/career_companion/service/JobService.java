package com.example.career_companion.service;

import com.example.career_companion.dto.JobRequest;
import com.example.career_companion.dto.JobResponse;
import com.example.career_companion.entity.Job;
import com.example.career_companion.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public JobResponse getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        return mapToResponse(job);
    }

    public List<JobResponse> getAllJobs() {

        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

        // CREATE
        public JobResponse createJob(JobRequest request) {
                Job job = new Job();

                job.setTitle(request.getTitle());
                job.setDescription(request.getDescription());
                job.setSalary(request.getSalary());
                job.setExperienceRequired(request.getExperienceRequired());
                job.setLocation(request.getLocation());
                job.setEmploymentType(request.getEmploymentType());
                job.setJobType(request.getJobType());
                job.setVacancies(request.getVacancies());
                job.setDeadline(request.getDeadline() != null ? java.time.LocalDateTime.of(request.getDeadline(), java.time.LocalTime.MIDNIGHT) : null);

                Job saved = jobRepository.save(job);
                return mapToResponse(saved);
        }

        // UPDATE
        public JobResponse updateJob(Long id, JobRequest request) {
                Job job = jobRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Job not found"));

                job.setTitle(request.getTitle());
                job.setDescription(request.getDescription());
                job.setSalary(request.getSalary());
                job.setExperienceRequired(request.getExperienceRequired());
                job.setLocation(request.getLocation());
                job.setEmploymentType(request.getEmploymentType());
                job.setJobType(request.getJobType());
                job.setVacancies(request.getVacancies());
                job.setDeadline(request.getDeadline() != null ? java.time.LocalDateTime.of(request.getDeadline(), java.time.LocalTime.MIDNIGHT) : job.getDeadline());

                Job updated = jobRepository.save(job);
                return mapToResponse(updated);
        }

    public void deleteJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        jobRepository.delete(job);
    }

    private JobResponse mapToResponse(Job job) {

        JobResponse response = new JobResponse();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setSalary(job.getSalary());
        response.setExperienceRequired(
                job.getExperienceRequired()
        );
        response.setLocation(job.getLocation());
        response.setEmploymentType(
                job.getEmploymentType()
        );
        response.setJobType(job.getJobType());
        response.setVacancies(job.getVacancies());
        response.setDeadline(job.getDeadline());

        if (job.getCompany() != null) {
            response.setCompanyName(
                    job.getCompany().getCompanyName()
            );

            response.setCompanyLogoUrl(
                    job.getCompany().getLogoUrl()
            );
        }

        return response;
    }
}