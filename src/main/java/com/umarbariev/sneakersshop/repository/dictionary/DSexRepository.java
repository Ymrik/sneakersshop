package com.umarbariev.sneakersshop.repository.dictionary;

import com.umarbariev.sneakersshop.model.entity.dictionary.SexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DSexRepository extends JpaRepository<SexEntity, Long> {
    Optional<SexEntity> findByCode(String code);
}
