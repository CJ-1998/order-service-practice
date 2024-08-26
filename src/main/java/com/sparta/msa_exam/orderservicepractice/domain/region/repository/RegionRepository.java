package com.sparta.msa_exam.orderservicepractice.domain.region.repository;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, UUID> {
    boolean existsByName(@NotBlank String name);
}
