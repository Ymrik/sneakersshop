package com.umarbariev.sneakersshop.mapper;

import com.umarbariev.sneakersshop.model.dto.dictionary.DCategoryDto;
import com.umarbariev.sneakersshop.model.entity.dictionary.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    DCategoryDto getFromEntity(CategoryEntity entity);
}
