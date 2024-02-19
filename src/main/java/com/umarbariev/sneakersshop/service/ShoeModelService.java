package com.umarbariev.sneakersshop.service;

import com.umarbariev.sneakersshop.mapper.ShoeModelMapper;
import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import com.umarbariev.sneakersshop.repository.dictionary.DShoeModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoeModelService {

    private final DShoeModelRepository shoeModelRepository;
    private final ShoeModelMapper shoeModelMapper;

    public List<DShoeModelDto> getAllShoes() {
        return shoeModelRepository.findAll().stream()
                                  .map(shoeModelMapper::fromEntity)
                                  .collect(Collectors.toList());
    }

}
