package com.umarbariev.sneakersshop.model.dto;

import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class OrderShoesDto {
    private DShoeModelDto shoeModelDto;
    private Long count;
}
