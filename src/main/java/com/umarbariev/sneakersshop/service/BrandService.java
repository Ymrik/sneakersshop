package com.umarbariev.sneakersshop.service;

import com.umarbariev.sneakersshop.mapper.BrandMapper;
import com.umarbariev.sneakersshop.model.dto.dictionary.DBrandDto;
import com.umarbariev.sneakersshop.repository.dictionary.DBrandEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final DBrandEntityRepository brandEntityRepository;
    private final BrandMapper brandMapper;

    public List<DBrandDto> getAllBrands() {
        return brandEntityRepository.findAll().stream()
                                    .map(brandMapper::fromEntity)
                                    .collect(Collectors.toList());
    }
}
