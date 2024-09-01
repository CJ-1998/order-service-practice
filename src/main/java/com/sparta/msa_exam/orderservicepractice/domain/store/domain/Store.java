package com.sparta.msa_exam.orderservicepractice.domain.store.domain;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.domain.review.domain.Review;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.CreateStoreRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.dto.request.UpdateStoreRequest;
import com.sparta.msa_exam.orderservicepractice.domain.store.domain.vo.StoreStatus;
import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_stores")
@SQLRestriction(value = "deleted_at is NULL")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    private String name;

    private String address;

    private StoreStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<StoreCategory> storeCategories = new ArrayList<>();

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

    @Builder
    public Store(String description, String address, String name, Region region, User user) {
        this.description = description;
        this.address = address;
        this.name = name;
        this.region = region;
        this.status = StoreStatus.ACTIVE;
        this.user = user;
    }

    public static Store from(CreateStoreRequest request, Region region, User user) {
        return Store.builder()
                .name(request.name())
                .description(request.description())
                .address(request.address())
                .region(region)
                .user(user)
                .build();
    }

    public void updateInfo(@Valid UpdateStoreRequest request, Region region) {
        this.name = request.name();
        this.description = request.description();
        this.address = request.address();
        this.region = region;
    }

    public void softDelete(String nickname) {
        this.deletedBy = nickname;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateStatus(@NotNull StoreStatus storeStatus) {
        this.status = storeStatus;
    }
}
