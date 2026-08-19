package com.example.career_companion.service;

import com.example.career_companion.dto.company.CompanyRequest;
import com.example.career_companion.dto.company.CompanyResponse;
import com.example.career_companion.entity.Company;
import com.example.career_companion.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    // CREATE
    public CompanyResponse createCompany(CompanyRequest request) {

        Company company = new Company();

        company.setCompanyName(request.getCompanyName());
        company.setDescription(request.getDescription());
        company.setWebsite(request.getWebsite());
        company.setLogoUrl(request.getLogoUrl());
        company.setIndustry(request.getIndustry());
        company.setLocation(request.getLocation());
        company.setEmployeeCount(request.getEmployeeCount());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());

        // Recruiter cannot verify their own company
        company.setVerified(false);

        Company savedCompany =
                companyRepository.save(company);

        return mapToResponse(savedCompany);
    }

    // GET BY ID
    public CompanyResponse getCompanyById(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        return mapToResponse(company);
    }

    // GET ALL
    public List<CompanyResponse> getAllCompanies() {

        return companyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // UPDATE
    public CompanyResponse updateCompany(
            Long id,
            CompanyRequest request) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        company.setCompanyName(request.getCompanyName());
        company.setDescription(request.getDescription());
        company.setWebsite(request.getWebsite());
        company.setLogoUrl(request.getLogoUrl());
        company.setIndustry(request.getIndustry());
        company.setLocation(request.getLocation());
        company.setEmployeeCount(request.getEmployeeCount());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());

        Company updatedCompany =
                companyRepository.save(company);

        return mapToResponse(updatedCompany);
    }

    // DELETE
    public void deleteCompany(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        companyRepository.delete(company);
    }

    // ENTITY -> DTO
    private CompanyResponse mapToResponse(Company company) {

        CompanyResponse response = new CompanyResponse();

        response.setId(company.getId());
        response.setCompanyName(company.getCompanyName());
        response.setDescription(company.getDescription());
        response.setWebsite(company.getWebsite());
        response.setLogoUrl(company.getLogoUrl());
        response.setIndustry(company.getIndustry());
        response.setLocation(company.getLocation());
        response.setEmployeeCount(company.getEmployeeCount());
        response.setEmail(company.getEmail());
        response.setPhone(company.getPhone());
        response.setVerified(company.getVerified());

        return response;
    }
}