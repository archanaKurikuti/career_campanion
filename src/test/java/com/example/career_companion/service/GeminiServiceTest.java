package com.example.career_companion.service;

import com.example.career_companion.service.ai.GeminiService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GeminiServiceTest {

    @Test
    void shouldTreatCommonGeminiPlaceholderValuesAsMissing() throws Exception {
        GeminiService service = new GeminiService(RestClient.builder());

        Method method = GeminiService.class.getDeclaredMethod("isPlaceholderApiKey", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(service, "your-real-key-here"));
        assertTrue((Boolean) method.invoke(service, "your_gemini_api_key_here"));
        assertTrue((Boolean) method.invoke(service, "replace-with-your-api-key"));
    }
}
