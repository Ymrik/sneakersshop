package com.umarbariev.sneakersshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    public static final String ADULT = "adult";
    public static final String CHILDREN = "children";

    private String category;
    private String brand;
    private String season;
    private String nameContains;
    private String sex;
    private Double minCost;
    private Double maxCost;
    private boolean premium;
    private String ageCategory;

    public String getDescription() {
        String description = "";
        if (!StringUtils.isEmpty(category)) {
            description = description.concat(String.format("Категория: %s", category));
        }
        if (!StringUtils.isEmpty(brand)) {
            description = description.concat(String.format(" Бренд: %s", brand));
        }
        if (!StringUtils.isEmpty(season)) {
            description = description.concat(String.format(" Сезон: %s", season));
        }
        if (!StringUtils.isEmpty(nameContains)) {
            description = description.concat(String.format(" Название: %s", nameContains));
        }
        if (!StringUtils.isEmpty(sex)) {
            description = description.concat(String.format(" Пол: %s", sex));
        }
        if (minCost != null && minCost != 0.0d) {
            description = description.concat(String.format(" Минимальная стоимость: %s", minCost));
        }
        if (maxCost != null && minCost != 0.0d) {
            description = description.concat(String.format(" Максимальная стоимость: %s", maxCost));
        }
        if (premium) {
            description = description.concat(" Только премиум");
        }
        if (ageCategory != null && ageCategory.equals(ADULT)) {
            description = description.concat(" Взрослая коллекция");
        } else if (ageCategory != null && ageCategory.equals(CHILDREN)){
            description = description.concat(" Детская коллекция");
        }

        return description;
    }
}
