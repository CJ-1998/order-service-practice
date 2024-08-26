package com.sparta.msa_exam.orderservicepractice.domain.review.domain.dtos;

import java.util.UUID;
import lombok.Builder;

@Builder
public class ReviewResponseDto {
    private UUID id;
    private UUID storeId;
    private Long userId;
    private int rating;
    private String comment;
    private boolean reported;
    private String reportMessage;
}
