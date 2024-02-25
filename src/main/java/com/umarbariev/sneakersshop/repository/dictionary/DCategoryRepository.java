package com.umarbariev.sneakersshop.repository.dictionary;

import com.umarbariev.sneakersshop.model.entity.dictionary.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByCode(String code);
}
