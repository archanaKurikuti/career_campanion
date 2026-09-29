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

    private final RestClient restClient;

    @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.model:gemini-3.5-flash}")
    private String model;

    public GeminiService(RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String generateAdvice(String prompt) {

        if (apiKey == null || apiKey.isBlank() || apiKey.equals("mock-key")) {
            return "Gemini API key is not configured.";
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
                    .uri("/v1beta/models/{model}:generateContent", model)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return "Gemini returned an empty response.";
            }

            Object candidatesObj = response.get("candidates");

            if (!(candidatesObj instanceof List<?> candidates)
                    || candidates.isEmpty()) {

                return "Gemini did not return a response.";
            }

            Object candidateObj = candidates.get(0);

            if (!(candidateObj instanceof Map<?, ?> candidate)) {
                return "Invalid Gemini response.";
            }

            Object contentObj = candidate.get("content");

            if (!(contentObj instanceof Map<?, ?> content)) {
                return "Gemini response content is missing.";
            }

            Object partsObj = content.get("parts");

            if (!(partsObj instanceof List<?> parts)
                    || parts.isEmpty()) {

                return "Gemini response text is missing.";
            }

            Object partObj = parts.get(0);

            if (!(partObj instanceof Map<?, ?> part)) {
                return "Invalid Gemini response part.";
            }

            Object text = part.get("text");

            if (text == null) {
                return "Gemini returned no text.";
            }

            return text.toString();

        } catch (RestClientException e) {

            return "Unable to contact Gemini: " + e.getMessage();
        }
    }
}