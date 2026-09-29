package com.example.career_companion.service;

import com.example.career_companion.dto.CandidateResponse;
import com.example.career_companion.dto.CandidateUpdateRequest;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Skill;
import com.example.career_companion.exception.ResourceNotFoundException;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;

    public CandidateService(CandidateRepository candidateRepository, SkillRepository skillRepository) {
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
    }

    public CandidateResponse getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        return mapToResponse(candidate);
    }

    public List<CandidateResponse> getAllCandidates() {
        return candidateRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CandidateResponse updateCandidate(Long id, CandidateUpdateRequest request) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

        if (request.getEducation() != null) candidate.setEducation(request.getEducation());
        if (request.getExperience() != null) candidate.setExperience(request.getExperience());
        if (request.getCurrentCompany() != null) candidate.setCurrentCompany(request.getCurrentCompany());
        if (request.getLocation() != null) candidate.setLocation(request.getLocation());
        if (request.getBio() != null) candidate.setBio(request.getBio());
        if (request.getGithub() != null) candidate.setGithub(request.getGithub());
        if (request.getLinkedin() != null) candidate.setLinkedin(request.getLinkedin());
        if (request.getPortfolio() != null) candidate.setPortfolio(request.getPortfolio());
        if (request.getExpectedSalary() != null) candidate.setExpectedSalary(request.getExpectedSalary());
        if (request.getOpenToWork() != null) candidate.setOpenToWork(request.getOpenToWork());

        if (request.getSkills() != null) {
            List<Skill> skills = new ArrayList<>();
            for (String skillName : request.getSkills()) {
                Skill skill = skillRepository.findByNameIgnoreCase(skillName.trim())
                        .orElseGet(() -> {
                            Skill s = new Skill();
                            s.setName(skillName.trim());
                            return skillRepository.save(s);
                        });
                skills.add(skill);
            }
            candidate.setSkills(skills);
        }

        Candidate savedCandidate = candidateRepository.save(candidate);
        return mapToResponse(savedCandidate);
    }

    public void deleteCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        candidateRepository.delete(candidate);
    }

    public CandidateResponse mapToResponse(Candidate candidate) {
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
        if (candidate.getSkills() != null) {
            response.setSkills(candidate.getSkills().stream().map(Skill::getName).toList());
        }
        return response;
    }
}