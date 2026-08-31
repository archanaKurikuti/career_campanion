package com.example.career_companion.service;

import com.example.career_companion.dto.AuthResponse;
import com.example.career_companion.dto.LoginRequest;
import com.example.career_companion.dto.RegisterRequest;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Recruiter;
import com.example.career_companion.entity.Role;
import com.example.career_companion.entity.User;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.RecruiterRepository;
import com.example.career_companion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email is already registered");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            role = Role.CANDIDATE;
        }

        User user;
        if (role == Role.RECRUITER) {
            Recruiter recruiter = new Recruiter();
            recruiter.setName(request.getName());
            recruiter.setEmail(request.getEmail());
            recruiter.setPhone(request.getPhone());
            recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
            recruiter.setRole(Role.RECRUITER);
            user = recruiterRepository.save(recruiter);
        } else {
            Candidate candidate = new Candidate();
            candidate.setName(request.getName());
            candidate.setEmail(request.getEmail());
            candidate.setPhone(request.getPhone());
            candidate.setPassword(passwordEncoder.encode(request.getPassword()));
            candidate.setRole(Role.CANDIDATE);
            user = candidateRepository.save(candidate);
        }

        String token = UUID.randomUUID().toString();
        return new AuthResponse(token, user.getRole().name(), user.getId(), user.getName());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = UUID.randomUUID().toString();
        return new AuthResponse(token, user.getRole() != null ? user.getRole().name() : "CANDIDATE", user.getId(), user.getName());
    }
}