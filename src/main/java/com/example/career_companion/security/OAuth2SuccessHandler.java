package com.example.career_companion.security;

import com.example.career_companion.entity.Candidate;
import com.example.career_companion.entity.Role;
import com.example.career_companion.entity.User;
import com.example.career_companion.repository.CandidateRepository;
import com.example.career_companion.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public OAuth2SuccessHandler(
            UserRepository userRepository,
            CandidateRepository candidateRepository,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null || email.trim().isEmpty()) {
            String login = oAuth2User.getAttribute("login");
            email = (login != null ? login : "oauth2_user_" + UUID.randomUUID()) + "@github.com";
        }

        if (name == null || name.trim().isEmpty()) {
            name = email.split("@")[0];
        }

        Optional<User> existingUserOpt = userRepository.findByEmail(email);
        User user;

        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
        } else {
            Candidate candidate = new Candidate();
            candidate.setName(name);
            candidate.setEmail(email);
            candidate.setPassword(UUID.randomUUID().toString());
            candidate.setRole(Role.CANDIDATE);
            candidate.setBio("Registered via OAuth2 SSO");
            user = candidateRepository.save(candidate);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails, user.getId(), user.getRole().name());

        String targetUrl = "http://localhost:5173/oauth2/redirect?token=" + token
                + "&role=" + user.getRole().name()
                + "&userId=" + user.getId()
                + "&name=" + URLEncoder.encode(user.getName(), StandardCharsets.UTF_8);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
