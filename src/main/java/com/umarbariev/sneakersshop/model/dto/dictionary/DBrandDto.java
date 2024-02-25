package com.umarbariev.sneakersshop.model.dto.dictionary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBrandDto {
    private Long id;
    private String code;
    private String name;
    private String description;
}
