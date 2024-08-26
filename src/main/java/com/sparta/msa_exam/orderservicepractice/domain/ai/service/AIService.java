package com.sparta.msa_exam.orderservicepractice.domain.ai.service;

import com.sparta.msa_exam.orderservicepractice.domain.ai.dto.ChatRequest;
import com.sparta.msa_exam.orderservicepractice.domain.ai.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AIService {

    @Qualifier("AIRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String getAIRequest(String prompt) {

        // Gemini에 요청 전송
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        ChatRequest request = new ChatRequest(prompt);
        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        return message;
    }
}
