package com.umarbariev.sneakersshop.mapper;

import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import com.umarbariev.sneakersshop.model.entity.dictionary.BrandEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.CategoryEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.SeasonEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.SexEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.ShoeModelEntity;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ShoeModelMapper {

    DShoeModelDto fromEntity(ShoeModelEntity entity);

    default String fromCategoryEntity(CategoryEntity entity) {
        return entity.getName();
    }

    default String fromBrandEntity(BrandEntity entity) {
        return entity.getName();
    }

    default String fromSexEntity(SexEntity entity) {
        return entity.getName();
    }

    default Double fromBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal.doubleValue();
    }

    default String fromSeasonsEntity(SeasonEntity entity) {
        return entity.getName();
    }
}

