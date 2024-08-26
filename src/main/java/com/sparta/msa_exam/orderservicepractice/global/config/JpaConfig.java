package com.sparta.msa_exam.orderservicepractice.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // TODO. SecurityContext 에서 꺼내온 Authentication 에서 nickname 추출
        return () -> Optional.of("닉네임");
    }
}
