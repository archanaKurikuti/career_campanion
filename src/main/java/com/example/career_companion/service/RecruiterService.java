package com.example.career_companion.service;

import com.example.career_companion.entity.Recruiter;
import com.example.career_companion.repository.RecruiterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    public Recruiter getRecruiterById(Long id) {

        return recruiterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Recruiter not found"));
    }

    public Recruiter updateRecruiter(Long id, Recruiter recruiter) {

        Recruiter existing =
                recruiterRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Recruiter not found"));

        existing.setName(recruiter.getName());
        existing.setEmail(recruiter.getEmail());

        return recruiterRepository.save(existing);
    }
}