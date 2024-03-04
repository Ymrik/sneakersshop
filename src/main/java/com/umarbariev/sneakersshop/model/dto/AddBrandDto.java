package com.umarbariev.sneakersshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddBrandDto {
    private String code;
    private String name;
    private String description;
    private String photoUrl;
}
