package com.example.career_companion.service;

import com.example.career_companion.dto.JobRequest;
import com.example.career_companion.dto.JobResponse;
import com.example.career_companion.entity.Company;
import com.example.career_companion.entity.Job;
import com.example.career_companion.entity.Recruiter;
import com.example.career_companion.exception.ResourceNotFoundException;
import com.example.career_companion.repository.CompanyRepository;
import com.example.career_companion.repository.JobRepository;
import com.example.career_companion.repository.RecruiterRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final RecruiterRepository recruiterRepository;

    public JobService(
            JobRepository jobRepository,
            CompanyRepository companyRepository,
            RecruiterRepository recruiterRepository
    ) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return mapToResponse(job);
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Page<JobResponse> searchJobs(
            String keyword,
            String location,
            Double minSalary,
            Integer maxExperience,
            String jobType,
            String employmentType,
            Long companyId,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Job> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                Predicate titleLike = cb.like(cb.lower(root.get("title")), pattern);
                Predicate descLike = cb.like(cb.lower(root.get("description")), pattern);
                predicates.add(cb.or(titleLike, descLike));
            }

            if (location != null && !location.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.trim().toLowerCase() + "%"));
            }

            if (minSalary != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salary"), minSalary));
            }

            if (maxExperience != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("experienceRequired"), maxExperience));
            }

            if (jobType != null && !jobType.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("jobType")), jobType.trim().toLowerCase()));
            }

            if (employmentType != null && !employmentType.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("employmentType")), employmentType.trim().toLowerCase()));
            }

            if (companyId != null) {
                predicates.add(cb.equal(root.get("company").get("id"), companyId));
            }

            predicates.add(cb.equal(root.get("isActive"), true));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return jobRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    public JobResponse createJob(JobRequest request, Long recruiterId) {
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setResponsibilities(request.getResponsibilities());
        job.setSalary(request.getSalary());
        job.setExperienceRequired(request.getExperienceRequired());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setJobType(request.getJobType());
        job.setVacancies(request.getVacancies() != null ? request.getVacancies() : 1);
        job.setIsActive(true);

        if (request.getDeadline() != null) {
            job.setDeadline(LocalDateTime.of(request.getDeadline(), LocalTime.MAX));
        }

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + request.getCompanyId()));
            job.setCompany(company);
        }

        if (recruiterId != null) {
            Recruiter recruiter = recruiterRepository.findById(recruiterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with id: " + recruiterId));
            job.setRecruiter(recruiter);
            if (job.getCompany() == null && recruiter.getCompany() != null) {
                job.setCompany(recruiter.getCompany());
            }
        }

        Job saved = jobRepository.save(job);
        return mapToResponse(saved);
    }

    public JobResponse updateJob(Long id, JobRequest request) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getRequirements() != null) job.setRequirements(request.getRequirements());
        if (request.getResponsibilities() != null) job.setResponsibilities(request.getResponsibilities());
        if (request.getSalary() != null) job.setSalary(request.getSalary());
        if (request.getExperienceRequired() != null) job.setExperienceRequired(request.getExperienceRequired());
        if (request.getLocation() != null) job.setLocation(request.getLocation());
        if (request.getEmploymentType() != null) job.setEmploymentType(request.getEmploymentType());
        if (request.getJobType() != null) job.setJobType(request.getJobType());
        if (request.getVacancies() != null) job.setVacancies(request.getVacancies());

        if (request.getDeadline() != null) {
            job.setDeadline(LocalDateTime.of(request.getDeadline(), LocalTime.MAX));
        }

        Job updated = jobRepository.save(job);
        return mapToResponse(updated);
    }

    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        jobRepository.delete(job);
    }

    public JobResponse mapToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setRequirements(job.getRequirements());
        response.setResponsibilities(job.getResponsibilities());
        response.setSalary(job.getSalary());
        response.setExperienceRequired(job.getExperienceRequired());
        response.setLocation(job.getLocation());
        response.setEmploymentType(job.getEmploymentType());
        response.setJobType(job.getJobType());
        response.setVacancies(job.getVacancies());
        response.setPostedDate(job.getPostedAt());
        response.setDeadline(job.getDeadline());
        response.setIsActive(job.getIsActive());
        response.setStatus(job.getIsActive() != null && job.getIsActive() ? "ACTIVE" : "CLOSED");

        if (job.getCompany() != null) {
            response.setCompanyId(job.getCompany().getId());
            response.setCompanyName(job.getCompany().getCompanyName());
            response.setCompanyLogoUrl(job.getCompany().getLogoUrl());
        }

        if (job.getRecruiter() != null) {
            response.setRecruiterId(job.getRecruiter().getId());
            response.setRecruiterName(job.getRecruiter().getName());
        }

        return response;
    }
}