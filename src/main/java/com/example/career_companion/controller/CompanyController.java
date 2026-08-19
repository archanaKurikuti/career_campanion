package com.example.career_companion.controller;

import com.example.career_companion.dto.CompanyRequest;
import com.example.career_companion.dto.CompanyResponse;
import com.example.career_companion.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;


    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @RequestBody CompanyRequest request) {

        return ResponseEntity.ok(
                companyService.createCompany(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                companyService.getCompanyById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {

        return ResponseEntity.ok(
                companyService.getAllCompanies()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long id,
            @RequestBody CompanyRequest request) {

        return ResponseEntity.ok(
                companyService.updateCompany(id, request)
        );
    }
}