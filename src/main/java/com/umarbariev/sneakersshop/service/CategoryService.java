package com.umarbariev.sneakersshop.service;

import com.umarbariev.sneakersshop.mapper.CategoryMapper;
import com.umarbariev.sneakersshop.model.dto.dictionary.DCategoryDto;
import com.umarbariev.sneakersshop.repository.dictionary.DCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final DCategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<DCategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                                 .map(categoryMapper::getFromEntity)
                                 .collect(Collectors.toList());
    }
}
