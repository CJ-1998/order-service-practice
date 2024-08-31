package com.sparta.msa_exam.orderservicepractice.global.base.init;

import com.sparta.msa_exam.orderservicepractice.domain.region.domain.Region;
import com.sparta.msa_exam.orderservicepractice.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class RegionInitializer implements CommandLineRunner {

    private final RegionRepository regionRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Region> regions = Arrays.asList(
                new Region("서울"),
                new Region("경기도"),
                new Region("대구")
        );

        if (regionRepository.count() == 0) {
            regionRepository.saveAll(regions);
        }
    }
}
