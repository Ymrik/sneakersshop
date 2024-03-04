package com.umarbariev.sneakersshop.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditShoeDto {
    private Long id;
    private String modelName;
    private String brandCode;
    private String categoryCode;
    private String sexCode;
    private Double cost;
    private boolean isPremium;
    private Boolean isAdult;
    private String seasonCode;
    private String photoUrl;
    private Long countInFirstStock;
    private Long countInSecondStock;
    private String description;
}
