package com.example.career_companion.service;

import com.example.career_companion.dto.job.JobRequest;
import com.example.career_companion.dto.job.JobResponse;
import com.example.career_companion.entity.Job;
import com.example.career_companion.repository.JobRepository;
import org.springframework.stereotype.Service;

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