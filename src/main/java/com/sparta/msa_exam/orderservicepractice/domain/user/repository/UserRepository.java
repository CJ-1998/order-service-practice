package com.sparta.msa_exam.orderservicepractice.domain.user.repository;

import com.sparta.msa_exam.orderservicepractice.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);
}
