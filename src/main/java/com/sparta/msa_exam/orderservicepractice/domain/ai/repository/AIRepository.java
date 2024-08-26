package com.sparta.msa_exam.orderservicepractice.domain.ai.repository;

import com.sparta.msa_exam.orderservicepractice.domain.ai.domain.AI;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIRepository extends JpaRepository<AI, UUID> {
}
