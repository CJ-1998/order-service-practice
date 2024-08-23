package com.sparta.msa_exam.orderservicepractice.domain.ai;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AIRepository extends JpaRepository<AI, UUID> {
}
