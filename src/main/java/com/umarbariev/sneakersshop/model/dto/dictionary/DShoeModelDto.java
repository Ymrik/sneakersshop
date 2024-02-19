package com.umarbariev.sneakersshop.model.dto.dictionary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DShoeModelDto {
    private Long id;
    private String category;
    private String brand;
    private String modelName;
    private String sex;
    private Double cost;
    private Boolean isAdult;
    private Boolean isPremium;
    private String description;
    private String seasons;
    private String photoUrl;
}
