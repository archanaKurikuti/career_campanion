package com.example.career_companion.service;

import com.example.career_companion.dto.AuthResponse;
import com.example.career_companion.dto.LoginRequest;
import com.example.career_companion.dto.RegisterRequest;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Recruiter;
import com.example.career_companion.entity.Role;
import com.example.career_companion.entity.User;
import com.example.career_companion.exception.DuplicateResourceException;
import com.example.career_companion.exception.UnauthorizedException;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.RecruiterRepository;
import com.example.career_companion.repository.UserRepository;
import com.example.career_companion.security.CustomUserDetailsService;
import com.example.career_companion.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(
            UserRepository userRepository,
            CandidateRepository candidateRepository,
            RecruiterRepository recruiterRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User with email " + request.getEmail() + " already exists");
        }

        String phone = request.getPhone();
        if (phone != null && phone.trim().isEmpty()) {
            phone = null;
        } else if (phone != null) {
            phone = phone.trim();
        }

        if (phone != null && userRepository.findByPhone(phone).isPresent()) {
            throw new DuplicateResourceException("User with phone number " + phone + " already exists");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            role = Role.CANDIDATE;
        }

        User savedUser;
        if (role == Role.RECRUITER) {
            Recruiter recruiter = new Recruiter();
            recruiter.setName(request.getName());
            recruiter.setEmail(request.getEmail());
            recruiter.setPhone(phone);
            recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
            recruiter.setRole(Role.RECRUITER);
            savedUser = recruiterRepository.save(recruiter);
        } else {
            Candidate candidate = new Candidate();
            candidate.setName(request.getName());
            candidate.setEmail(request.getEmail());
            candidate.setPhone(phone);
            candidate.setPassword(passwordEncoder.encode(request.getPassword()));
            candidate.setRole(Role.CANDIDATE);
            savedUser = candidateRepository.save(candidate);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtService.generateToken(userDetails, savedUser.getId(), savedUser.getRole().name());

        return new AuthResponse(token, savedUser.getRole().name(), savedUser.getId(), savedUser.getName());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String roleStr = user.getRole() != null ? user.getRole().name() : "CANDIDATE";
        String token = jwtService.generateToken(userDetails, user.getId(), roleStr);

        return new AuthResponse(token, roleStr, user.getId(), user.getName());
    }
}