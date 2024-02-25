package com.umarbariev.sneakersshop.repository.dictionary;

import com.umarbariev.sneakersshop.model.entity.dictionary.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DBrandEntityRepository extends JpaRepository<BrandEntity, Long> {
    Optional<BrandEntity> findByCode(String code);
}
