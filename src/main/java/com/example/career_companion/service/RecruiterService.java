package com.example.career_companion.service;

import com.example.career_companion.dto.RecruiterResponse;
import com.example.career_companion.dto.RecruiterUpdateRequest;
import com.example.career_companion.entity.Company;
import com.example.career_companion.entity.Recruiter;
import com.example.career_companion.exception.ResourceNotFoundException;
import com.example.career_companion.repository.CompanyRepository;
import com.example.career_companion.repository.RecruiterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;

    public RecruiterService(RecruiterRepository recruiterRepository, CompanyRepository companyRepository) {
        this.recruiterRepository = recruiterRepository;
        this.companyRepository = companyRepository;
    }

    public RecruiterResponse getRecruiterById(Long id) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with id: " + id));
        return mapToResponse(recruiter);
    }

    public List<RecruiterResponse> getAllRecruiters() {
        return recruiterRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RecruiterResponse updateRecruiter(Long id, RecruiterUpdateRequest request) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with id: " + id));

        if (request.getName() != null) recruiter.setName(request.getName());
        if (request.getPhone() != null) recruiter.setPhone(request.getPhone());
        if (request.getDesignation() != null) recruiter.setDesignation(request.getDesignation());
        if (request.getDepartment() != null) recruiter.setDepartment(request.getDepartment());

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + request.getCompanyId()));
            recruiter.setCompany(company);
        }

        Recruiter saved = recruiterRepository.save(recruiter);
        return mapToResponse(saved);
    }

    public RecruiterResponse mapToResponse(Recruiter recruiter) {
        RecruiterResponse response = new RecruiterResponse();
        response.setId(recruiter.getId());
        response.setName(recruiter.getName());
        response.setEmail(recruiter.getEmail());
        response.setPhone(recruiter.getPhone());
        response.setDesignation(recruiter.getDesignation());
        response.setDepartment(recruiter.getDepartment());
        if (recruiter.getCompany() != null) {
            response.setCompanyId(recruiter.getCompany().getId());
            response.setCompanyName(recruiter.getCompany().getCompanyName());
        }
        return response;
    }
}