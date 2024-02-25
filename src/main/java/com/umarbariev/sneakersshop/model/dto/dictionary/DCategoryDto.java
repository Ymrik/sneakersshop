package com.umarbariev.sneakersshop.model.dto.dictionary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DCategoryDto {
    private Long id;
    private String code;
    private String name;
}
