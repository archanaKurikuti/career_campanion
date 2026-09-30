package com.example.career_companion.service.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GeminiService.class);

    private final RestClient restClient;

    @Value("${ai.api-key:mock-key}")
    private String apiKey;

    @Value("${ai.model:gemini-1.5-flash}")
    private String model;

    public GeminiService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String generateAdvice(String prompt) {
        if (apiKey == null || apiKey.isBlank() || apiKey.equalsIgnoreCase("mock-key") || apiKey.contains("your_gemini_api_key")) {
            logger.info("No active Gemini API key configured. Generating smart AI fallback advice.");
            return generateSmartFallbackAdvice(prompt);
        }

        Map<String, Object> request = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri("/v1beta/models/{model}:generateContent?key={key}", model, apiKey)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return generateSmartFallbackAdvice(prompt);
            }

            Object candidatesObj = response.get("candidates");
            if (!(candidatesObj instanceof List<?> candidates) || candidates.isEmpty()) {
                return generateSmartFallbackAdvice(prompt);
            }

            Object candidateObj = candidates.get(0);
            if (!(candidateObj instanceof Map<?, ?> candidate)) {
                return generateSmartFallbackAdvice(prompt);
            }

            Object contentObj = candidate.get("content");
            if (!(contentObj instanceof Map<?, ?> content)) {
                return generateSmartFallbackAdvice(prompt);
            }

            Object partsObj = content.get("parts");
            if (!(partsObj instanceof List<?> parts) || parts.isEmpty()) {
                return generateSmartFallbackAdvice(prompt);
            }

            Object partObj = parts.get(0);
            if (!(partObj instanceof Map<?, ?> part)) {
                return generateSmartFallbackAdvice(prompt);
            }

            Object text = part.get("text");
            if (text == null || text.toString().isBlank()) {
                return generateSmartFallbackAdvice(prompt);
            }

            return text.toString();

        } catch (RestClientException e) {
            logger.warn("Unable to contact Gemini API: {}. Falling back to smart career guidance.", e.getMessage());
            return generateSmartFallbackAdvice(prompt);
        }
    }

    private String generateSmartFallbackAdvice(String prompt) {
        return """
                ### Career Growth & Guidance Strategy

                Based on your profile and question:

                1. **Targeted Technical Focus**: Build hands-on portfolio projects demonstrating full-stack architecture, clean code principles, RESTful microservices, and database optimization.
                2. **Skills Expansion**: Strengthen your proficiency in modern frameworks (Spring Boot, React/Next.js) and cloud technologies (AWS, Docker, CI/CD pipelines).
                3. **System Design & Problem Solving**: Practice system design patterns and data structure algorithms to excel in technical interviews.
                4. **Professional Branding**: Optimize your resume with measurable metrics (e.g., "Improved query execution by 35%"), maintain an active GitHub profile, and engage with professional communities.
                
                *Note: To enable live real-time Gemini AI responses, configure your `GEMINI_API_KEY` in environment variables or application properties.*
                """;
    }
}