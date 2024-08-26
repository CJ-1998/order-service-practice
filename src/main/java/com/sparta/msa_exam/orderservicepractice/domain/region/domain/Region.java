package com.sparta.msa_exam.orderservicepractice.domain.region.domain;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.dto.request.CreateRegionRequest;
import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "regions")
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
}
