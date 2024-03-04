package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.OrderShoesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderShoesInStockRepository extends JpaRepository<OrderShoesEntity, Long> {
    List<OrderShoesEntity> getAllByOrder_Id(Long orderId);

    @Query(value = "select case when count(*)>0 then true else false end from sneakers_shop.orders where id in (select order_id from sneakers_shop.order_shoes where shoes_in_stock_id in (select id from sneakers_shop.shoes_in_stock where shoe_model_id = ?1)) and order_status in ('CREATED', 'FILLING', 'DELIVERING', 'DELIVERED')",
            nativeQuery = true)
    Boolean existActiveOrdersWithGivenShoeId(Long shoeId);

    @Query(value = "select order_id from sneakers_shop.order_shoes where shoes_in_stock_id in (?1)", nativeQuery = true)
    List<Long> getAllOrderIdsByShoesInStockIds(List<Long> shoeInStockIds);

    @Modifying
    @Query(value = "delete from sneakers_shop.order_shoes where shoes_in_stock_id in (?1)", nativeQuery = true)
    void deleteAllByShoeInStockIds(List<Long> shoesInStockIds);
}
