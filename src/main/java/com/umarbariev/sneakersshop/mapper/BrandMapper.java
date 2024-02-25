package com.umarbariev.sneakersshop.mapper;

import com.umarbariev.sneakersshop.model.dto.dictionary.DBrandDto;
import com.umarbariev.sneakersshop.model.entity.dictionary.BrandEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    DBrandDto fromEntity(BrandEntity entity);
}
