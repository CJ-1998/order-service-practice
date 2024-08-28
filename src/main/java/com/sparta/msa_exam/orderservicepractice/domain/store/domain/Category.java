package com.sparta.msa_exam.orderservicepractice.domain.store.domain;

import com.sparta.msa_exam.orderservicepractice.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_categories")
@SQLRestriction(value = "deleted_at is NULL")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 45)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<StoreCategory> storeCategories = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public void updateName(@NotBlank String name) {
        this.name = name;
    }

    public void softDelete(String nickname) {
        this.deletedBy = nickname;
        this.deletedAt = LocalDateTime.now();
    }
}
