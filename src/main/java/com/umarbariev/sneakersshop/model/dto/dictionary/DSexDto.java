package com.umarbariev.sneakersshop.model.dto.dictionary;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DSexDto {
    public static final DSexDto MALE = new DSexDto("M", "Мужчина");
    public static final DSexDto FEMALE = new DSexDto("F", "Женщина");

    private String code;
    private String name;
}
