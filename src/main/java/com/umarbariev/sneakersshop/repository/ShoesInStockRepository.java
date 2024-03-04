package com.umarbariev.sneakersshop.repository;

import com.umarbariev.sneakersshop.model.entity.ShoesInStockEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.ShoeModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoesInStockRepository extends JpaRepository<ShoesInStockEntity, Long> {
    List<ShoesInStockEntity> getAllByShoeModelEntity(ShoeModelEntity shoeModelEntity);
    List<ShoesInStockEntity> getAllByShoeModelEntityId(Long id);

    @Query(value = "select count from sneakers_shop.shoes_in_stock where shoe_model_id=?1 and stockroom_id=?2",
            nativeQuery = true)
    Long getCountInStock(Long shoeId, Long stockId);

    @Query(value = "select id from sneakers_shop.shoes_in_stock where shoe_model_id=?1", nativeQuery = true)
    List<Long> getIdsWithGivenShoeId(Long shoeId);

    @Modifying
    @Query(value = "delete from sneakers_shop.shoes_in_stock where id in(?1)", nativeQuery = true)
    void deleteAllByIds(List<Long> ids);
}
