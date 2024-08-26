package com.sparta.msa_exam.orderservicepractice.domain.store.domain;

import com.sparta.msa_exam.orderservicepractice.domain.review.domain.Review;
import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stores")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @Transient
    private double averageRating;

    public double getAverageRating() {
        if (!reviews.isEmpty()) {
            calculateAverageRating();
        }
        return this.averageRating;
    }

    // 평균 리뷰 평점 계산 메서드
    public void calculateAverageRating() {
        this.averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public Store(String name) {
        this.name = name;
    }
}
