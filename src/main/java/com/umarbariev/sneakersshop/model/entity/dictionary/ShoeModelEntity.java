package com.umarbariev.sneakersshop.model.entity.dictionary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "d_shoe_models")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoeModelEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category", referencedColumnName = "code")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand", referencedColumnName = "code")
    private BrandEntity brand;

    @Column(name = "model_name")
    private String modelName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sex", referencedColumnName = "code")
    private SexEntity sex;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "is_premium")
    private Boolean isPremium;

    @Column(name = "is_adult")
    private Boolean isAdult;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seasons", referencedColumnName = "code")
    private SeasonEntity seasons;

    @Column(name = "photo_url")
    private String photoUrl;
}
