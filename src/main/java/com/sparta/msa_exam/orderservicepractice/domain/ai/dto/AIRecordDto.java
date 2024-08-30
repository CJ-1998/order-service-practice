package com.sparta.msa_exam.orderservicepractice.domain.ai.dto;

import com.sparta.msa_exam.orderservicepractice.domain.ai.domain.AI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIRecordDto {
    private String request;
    private String response;

    public static AIRecordDto convertToAIRecordDto(AI ai) {
        return AIRecordDto.builder()
                .request(ai.getRequest())
                .response(ai.getResponse())
                .build();
    }
}
