package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.StockroomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockroomRepository extends JpaRepository<StockroomEntity, Long> {
}
