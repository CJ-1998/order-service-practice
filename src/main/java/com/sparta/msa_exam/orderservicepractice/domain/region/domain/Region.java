package com.sparta.msa_exam.orderservicepractice.domain.region.domain;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request.CreateRegionRequest;
import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_regions")
@SQLRestriction(value = "deleted_at is NULL")
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public Region(String name) {
        this.name = name;
    }

    public static Region from(@Valid CreateRegionRequest request) {
        return new Region(request.name());
    }

    public void updateName(@NotBlank String name) {
        this.name = name;
    }

    public void softDelete(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }
}
