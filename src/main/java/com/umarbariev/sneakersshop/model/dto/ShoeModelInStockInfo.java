package com.umarbariev.sneakersshop.model.dto;

import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoeModelInStockInfo {
    private DShoeModelDto shoeModelDto;
    private Map<String, Long> countByStock;
}
