package com.umarbariev.sneakersshop.model.entity;

import com.umarbariev.sneakersshop.model.entity.dictionary.ShoeModelEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shoes_in_stock")
public class ShoesInStockEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shoe_model_id", referencedColumnName = "id")
    private ShoeModelEntity shoeModelEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stockroom_id", referencedColumnName = "id")
    private StockroomEntity stockroomEntity;

    @Column(name = "count")
    private Long count;
}
