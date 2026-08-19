package com.example.career_companion.service;

import com.example.career_companion.dto.candidate.CandidateResponse;
import com.example.career_companion.dto.candidate.CandidateUpdateRequest;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;


    // GET candidate by ID
    public CandidateResponse getCandidateById(Long id) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        return mapToResponse(candidate);
    }

    // GET all candidates
    public List<CandidateResponse> getAllCandidates() {

        return candidateRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // UPDATE candidate
    public CandidateResponse updateCandidate(
            Long id,
            CandidateUpdateRequest request) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        candidate.setEducation(request.getEducation());
        candidate.setExperience(request.getExperience());
        candidate.setCurrentCompany(request.getCurrentCompany());
        candidate.setLocation(request.getLocation());
        candidate.setBio(request.getBio());
        candidate.setGithub(request.getGithub());
        candidate.setLinkedin(request.getLinkedin());
        candidate.setPortfolio(request.getPortfolio());
        candidate.setExpectedSalary(request.getExpectedSalary());
        candidate.setOpenToWork(request.getOpenToWork());

        Candidate savedCandidate =
                candidateRepository.save(candidate);

        return mapToResponse(savedCandidate);
    }

    // DELETE candidate
    public void deleteCandidate(Long id) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        candidateRepository.delete(candidate);
    }

    // Entity -> DTO
    private CandidateResponse mapToResponse(Candidate candidate) {

        CandidateResponse response = new CandidateResponse();

        response.setId(candidate.getId());
        response.setName(candidate.getName());
        response.setEmail(candidate.getEmail());
        response.setEducation(candidate.getEducation());
        response.setExperience(candidate.getExperience());
        response.setCurrentCompany(candidate.getCurrentCompany());
        response.setLocation(candidate.getLocation());
        response.setBio(candidate.getBio());
        response.setGithub(candidate.getGithub());
        response.setLinkedin(candidate.getLinkedin());
        response.setPortfolio(candidate.getPortfolio());
        response.setExpectedSalary(candidate.getExpectedSalary());
        response.setOpenToWork(candidate.getOpenToWork());

        return response;
    }
}