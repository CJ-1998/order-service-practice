package com.sparta.msa_exam.orderservicepractice.domain.review.controller;

import com.sparta.msa_exam.orderservicepractice.domain.review.domain.Review;
import com.sparta.msa_exam.orderservicepractice.domain.review.domain.dtos.ReviewRequestDto;
import com.sparta.msa_exam.orderservicepractice.domain.review.domain.dtos.ReviewResponseDto;
import com.sparta.msa_exam.orderservicepractice.domain.review.domain.mapper.ReviewMapper;
import com.sparta.msa_exam.orderservicepractice.domain.review.service.ReviewService;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseBody;
import com.sparta.msa_exam.orderservicepractice.global.base.dto.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ResponseBody<ReviewResponseDto>> addReview(
            @RequestBody @Valid ReviewRequestDto reviewRequestDto) {

        Review review = reviewMapper.toReview(reviewRequestDto);
        Review createdReview = reviewService.addReview(review.getStore().getId(), review.getUser().getId(), review.getRating(), review.getComment());
        ReviewResponseDto responseDto = reviewMapper.toReviewResponseDto(createdReview);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ResponseBody<Page<ReviewResponseDto>>> getReviewsByStoreId(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue = "false") boolean includeReported,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        // 페이지 크기 제한: 10, 30, 50 이외의 값은 10으로 고정
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        // 정렬 방향 처리
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        } catch (IllegalArgumentException e) {
            direction = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Review> reviewPage = reviewService.getReviewsByStore(storeId, includeReported, pageable);
        Page<ReviewResponseDto> responseDtos = reviewPage.map(reviewMapper::toReviewResponseDto);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDtos));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ResponseBody<ReviewResponseDto>> getReviewById(
            @PathVariable UUID reviewId) {

        Review review = reviewService.getReviewById(reviewId);
        ReviewResponseDto responseDto = reviewMapper.toReviewResponseDto(review);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(responseDto));
    }

    @PostMapping("/{reviewId}/report")
    public ResponseEntity<ResponseBody<Void>> reportReview(
            @PathVariable UUID reviewId,
            @RequestParam String reportMessage) {
        reviewService.reportReview(reviewId, reportMessage);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(null));
    }
}
