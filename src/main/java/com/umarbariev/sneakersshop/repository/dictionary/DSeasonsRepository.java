package com.umarbariev.sneakersshop.repository.dictionary;

import com.umarbariev.sneakersshop.model.entity.dictionary.SeasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DSeasonsRepository extends JpaRepository<SeasonEntity, Long> {
    Optional<SeasonEntity> findByCode(String code);
}
