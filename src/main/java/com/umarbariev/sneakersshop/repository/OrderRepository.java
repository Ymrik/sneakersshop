package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> getAllByClientEntity_User_Username(String username);
}
