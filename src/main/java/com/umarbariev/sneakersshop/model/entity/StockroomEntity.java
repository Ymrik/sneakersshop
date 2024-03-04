package com.umarbariev.sneakersshop.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "stockrooms")
public class StockroomEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;
}
