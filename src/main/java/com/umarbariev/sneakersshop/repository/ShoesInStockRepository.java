package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.ShoesInStockEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.ShoeModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoesInStockRepository extends JpaRepository<ShoesInStockEntity, Long> {
    List<ShoesInStockEntity> getAllByShoeModelEntity(ShoeModelEntity shoeModelEntity);
}
