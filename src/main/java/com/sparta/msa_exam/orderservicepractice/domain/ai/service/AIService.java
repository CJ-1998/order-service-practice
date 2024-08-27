package com.sparta.msa_exam.orderservicepractice.domain.ai.service;

import com.sparta.msa_exam.orderservicepractice.domain.ai.domain.AI;
import com.sparta.msa_exam.orderservicepractice.domain.ai.dto.ChatRequest;
import com.sparta.msa_exam.orderservicepractice.domain.ai.dto.ChatResponse;
import com.sparta.msa_exam.orderservicepractice.domain.ai.repository.AIRepository;
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

    private final AIRepository aiRepository;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String LIMIT_MESSAGE = "답변을 최대한 간결하게 한 문장으로 100자 이하로";

    public String getAIRequest(String requestMessage) {

        // Gemini에 요청 전송
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        ChatRequest request = new ChatRequest(requestMessage + LIMIT_MESSAGE);
        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);

        String responseMessage = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        aiRepository.save(AI.createAI(requestMessage, responseMessage));

        return responseMessage;
    }
}
