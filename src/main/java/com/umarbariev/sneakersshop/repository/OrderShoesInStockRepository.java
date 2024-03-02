package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.OrderShoesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderShoesInStockRepository extends JpaRepository<OrderShoesEntity, Long> {
    List<OrderShoesEntity> getAllByOrder_Id(Long orderId);
}
