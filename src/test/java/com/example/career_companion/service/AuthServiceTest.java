package com.example.career_companion.service;

import com.example.career_companion.dto.AuthResponse;
import com.example.career_companion.dto.LoginRequest;
import com.example.career_companion.dto.RegisterRequest;
import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Role;
import com.example.career_companion.entity.User;
import com.example.career_companion.exception.DuplicateResourceException;
import com.example.career_companion.exception.UnauthorizedException;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.RecruiterRepository;
import com.example.career_companion.repository.UserRepository;
import com.example.career_companion.security.CustomUserDetailsService;
import com.example.career_companion.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");
        request.setRole("CANDIDATE");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("John Doe");
        candidate.setEmail("john@example.com");
        candidate.setRole(Role.CANDIDATE);

        when(candidateRepository.save(any(Candidate.class))).thenReturn(candidate);
        when(userDetailsService.loadUserByUsername(request.getEmail())).thenReturn(mock(UserDetails.class));
        when(jwtService.generateToken(any(), eq(1L), eq("CANDIDATE"))).thenReturn("mockedJwtToken");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mockedJwtToken", response.getToken());
        assertEquals("CANDIDATE", response.getRole());
        assertEquals(1L, response.getUserId());
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateResourceException.class, () -> authService.register(request));
    }

    @Test
    void register_DuplicatePhone_ThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPhone("1234567890");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhone("1234567890")).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateResourceException.class, () -> authService.register(request));
    }

    @Test
    void register_EmptyPhone_NormalizesToNull() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");
        request.setRole("CANDIDATE");
        request.setPhone("   ");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("John Doe");
        candidate.setEmail("john@example.com");
        candidate.setPhone(null);
        candidate.setRole(Role.CANDIDATE);

        when(candidateRepository.save(any(Candidate.class))).thenAnswer(invocation -> {
            Candidate saved = invocation.getArgument(0);
            assertNull(saved.getPhone());
            return candidate;
        });
        when(userDetailsService.loadUserByUsername(request.getEmail())).thenReturn(mock(UserDetails.class));
        when(jwtService.generateToken(any(), eq(1L), eq("CANDIDATE"))).thenReturn("mockedJwtToken");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        verify(candidateRepository).save(argThat(c -> c.getPhone() == null));
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        Candidate user = new Candidate();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.CANDIDATE);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(userDetailsService.loadUserByUsername(request.getEmail())).thenReturn(mock(UserDetails.class));
        when(jwtService.generateToken(any(), eq(1L), eq("CANDIDATE"))).thenReturn("mockedJwtToken");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mockedJwtToken", response.getToken());
    }

    @Test
    void login_InvalidPassword_ThrowsUnauthorizedException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("wrongPassword");

        Candidate user = new Candidate();
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(request));
    }
}
