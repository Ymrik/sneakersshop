package com.umarbariev.sneakersshop.model.dto;

import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientFavouritesDto {
    private String username;
    private List<DShoeModelDto> shoeModels;
}
