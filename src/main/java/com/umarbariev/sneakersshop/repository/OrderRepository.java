package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> getAllByClientEntity_User_Username(String username);

    @Modifying
    @Query(value = "delete from sneakers_shop.orders where id in (?1)", nativeQuery = true)
    void deleteAllByIds(List<Long> ids);
}
