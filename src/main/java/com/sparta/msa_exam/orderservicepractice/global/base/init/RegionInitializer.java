package com.sparta.msa_exam.orderservicepractice.global.base.init;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RegionInitializer implements CommandLineRunner {

    private final RegionRepository regionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (regionRepository.count() == 0) {
            regionRepository.save(new Region("광화문"));
        }
    }
}
