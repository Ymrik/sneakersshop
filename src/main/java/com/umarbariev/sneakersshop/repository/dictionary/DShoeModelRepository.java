package com.umarbariev.sneakersshop.repository.dictionary;

import com.umarbariev.sneakersshop.model.entity.dictionary.ShoeModelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DShoeModelRepository extends JpaRepository<ShoeModelEntity, Long>, JpaSpecificationExecutor<ShoeModelEntity> {
    Page<ShoeModelEntity> findAll(Specification<ShoeModelEntity> spec, Pageable pageable);

    List<ShoeModelEntity> findAll(Specification<ShoeModelEntity> spec);
}
