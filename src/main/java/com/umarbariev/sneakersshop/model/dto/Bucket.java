package com.umarbariev.sneakersshop.model.dto;

import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Bucket {
    Map<DShoeModelDto, Long> shoes;

    public void addShoes(DShoeModelDto shoeModelDto, Long count) {
        if (shoes == null) {
            shoes = new HashMap<>();
        }

        shoes.compute(shoeModelDto, (k,v) -> v == null ? count : v + count);
    }

    public void subtractShoes(DShoeModelDto shoeModelDto, Long count) {
        if (shoes == null || !shoes.containsKey(shoeModelDto)) {
            return;
        }
        if (shoes.get(shoeModelDto) <= count) {
            shoes.remove(shoeModelDto);
            return;
        }

        shoes.compute(shoeModelDto, (k,v) -> v == null ? 0L : v - count);
    }

    @Override
    public String toString() {
        return shoes.toString();
    }

}
