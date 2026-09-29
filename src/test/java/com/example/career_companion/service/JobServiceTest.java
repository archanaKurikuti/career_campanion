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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @InjectMocks
    private JobService jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getJobById_Success() {
        Job job = new Job();
        job.setId(10L);
        job.setTitle("Senior Java Engineer");

        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        JobResponse response = jobService.getJobById(10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Senior Java Engineer", response.getTitle());
    }

    @Test
    void getJobById_NotFound_ThrowsException() {
        when(jobRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jobService.getJobById(99L));
    }

    @Test
    void createJob_Success() {
        JobRequest request = new JobRequest();
        request.setTitle("Backend Engineer");
        request.setDescription("Build scalable APIs");
        request.setSalary(120000.0);
        request.setCompanyId(1L);

        Company company = new Company();
        company.setId(1L);
        company.setCompanyName("TechCorp");

        Recruiter recruiter = new Recruiter();
        recruiter.setId(5L);
        recruiter.setName("Alice");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(recruiterRepository.findById(5L)).thenReturn(Optional.of(recruiter));

        Job savedJob = new Job();
        savedJob.setId(100L);
        savedJob.setTitle("Backend Engineer");
        savedJob.setCompany(company);
        savedJob.setRecruiter(recruiter);

        when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

        JobResponse response = jobService.createJob(request, 5L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("TechCorp", response.getCompanyName());
    }
}
