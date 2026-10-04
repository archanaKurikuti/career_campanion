package com.example.career_companion.service.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class GeminiService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GeminiService.class);

    private final RestClient restClient;

    @Value("${ai.api-key:mock-key}")
    private String apiKey;

    @Value("${ai.model:gemini-3.8-flash}")
    private String model;

    @Value("${ai.retry.attempts:4}")
    private int maxAttempts;

    @Value("${ai.retry.baseDelayMs:500}")
    private long baseDelayMs;

    public GeminiService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String generateAdvice(String prompt) {
        if (isPlaceholderApiKey(apiKey)) {
            logger.info("No active Gemini API key configured. Generating smart AI fallback advice.");
            return generateSmartFallbackAdvice(prompt, "No active Gemini API key configured.");
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

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                Map<?, ?> response = restClient.post()
                        .uri("/v1beta/models/{model}:generateContent?key={key}", model, apiKey)
                        .header("x-goog-api-key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .body(Map.class);

                if (response == null) {
                    throw new RestClientException("Empty response");
                }

                Object candidatesObj = response.get("candidates");
                if (!(candidatesObj instanceof List<?> candidates) || candidates.isEmpty()) {
                    throw new RestClientException("No candidates in response");
                }

                Object candidateObj = candidates.get(0);
                if (!(candidateObj instanceof Map<?, ?> candidate)) {
                    throw new RestClientException("Invalid candidate format");
                }

                Object contentObj = candidate.get("content");
                if (!(contentObj instanceof Map<?, ?> content)) {
                    throw new RestClientException("Missing content");
                }

                Object partsObj = content.get("parts");
                if (!(partsObj instanceof List<?> parts) || parts.isEmpty()) {
                    throw new RestClientException("No parts in content");
                }

                Object partObj = parts.get(0);
                if (!(partObj instanceof Map<?, ?> part)) {
                    throw new RestClientException("Invalid part format");
                }

                Object text = part.get("text");
                if (text == null || text.toString().isBlank()) {
                    throw new RestClientException("Empty text");
                }

                return text.toString();

            } catch (RestClientException e) {
                boolean isLast = attempt == maxAttempts;
                logger.warn("Attempt {}/{}: Gemini API request failed: {}", attempt, maxAttempts, e.getMessage());

                if (isLast) {
                    logger.warn("All attempts failed. Falling back to smart AI advice.");
                    return generateSmartFallbackAdvice(prompt, e.getMessage());
                }

                // exponential backoff with jitter
                long delay = baseDelayMs * (1L << (attempt - 1));
                long jitter = (long) (Math.random() * 200L);
                long sleep = Math.min(delay + jitter, 10_000L);

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return generateSmartFallbackAdvice(prompt, e.getMessage());
                }
            }
        }

        return generateSmartFallbackAdvice(prompt, "Gemini request failed after retries.");
    }

    boolean isPlaceholderApiKey(String apiKeyValue) {
        if (apiKeyValue == null) {
            return true;
        }

        String normalized = apiKeyValue.trim();
        if (normalized.isBlank()) {
            return true;
        }

        String lower = normalized.toLowerCase(Locale.ROOT);
        return lower.contains("mock")
                || lower.contains("your_")
                || lower.contains("your-")
                || lower.contains("replace")
                || lower.contains("example")
                || lower.contains("placeholder")
                || lower.contains("dummy")
                || lower.contains("test-key");
    }

    private String generateSmartFallbackAdvice(String prompt, String errorMessage) {
        String statusNote = (errorMessage == null || errorMessage.isBlank())
                ? "\n\n*Live Gemini responses are unavailable right now, so the app is using built-in guidance until the API becomes available.*"
                : "\n\n> Gemini is currently unavailable: " + errorMessage + "\n> The app is using built-in guidance until the API is working again.";

        return """
                ### Career Growth Roadmap

                Based on your profile and question, here is a practical path forward:

                1. **Leverage your current strengths**: Focus on the technologies and domains you already know, then add one or two adjacent skills that increase your market value.
                2. **Build portfolio proof**: Ship 2–3 projects that demonstrate end-to-end product delivery, clean architecture, APIs, database design, and deployment workflows.
                3. **Target the right role**: Align your profile with a specific role such as Java Backend Developer, Full Stack Engineer, or Software Engineer, and tailor your resume for that path.
                4. **Improve your resume**: Replace generic statements with measurable outcomes like "Built REST APIs serving 10,000+ requests/day" or "Reduced deployment time by 40%."
                5. **Prepare for interviews**: Practice problem solving, system design basics, and behavioral stories using the STAR format.
                6. **Expand relevant skills**: Prioritize technologies that match your target role, such as Spring Boot, React, Java, SQL, Docker, cloud basics, and API design.
                7. **Network and apply strategically**: Apply consistently to jobs that match your profile, and keep your GitHub, LinkedIn, and project portfolio current.

                ### 30-Day Action Plan
                - Week 1: Update resume, identify target job roles, and clean up portfolio.
                - Week 2: Complete one hands-on project and document the architecture.
                - Week 3: Practice coding interviews and system design fundamentals.
                - Week 4: Apply to relevant roles and follow up with recruiters.

                ### Guidance
                The best next move is to turn your current skills into evidence: projects, measurable outcomes, and a focused job target. That combination is what recruiters and hiring managers respond to most strongly.
                """ + statusNote;
    }
}