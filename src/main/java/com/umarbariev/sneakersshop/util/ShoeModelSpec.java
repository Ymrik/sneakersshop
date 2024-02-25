package com.umarbariev.sneakersshop.util;

import com.umarbariev.sneakersshop.model.dto.SearchCriteria;
import com.umarbariev.sneakersshop.model.entity.dictionary.BrandEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.CategoryEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.SeasonEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.SexEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.ShoeModelEntity;
import com.umarbariev.sneakersshop.repository.dictionary.DBrandEntityRepository;
import com.umarbariev.sneakersshop.repository.dictionary.DCategoryRepository;
import com.umarbariev.sneakersshop.repository.dictionary.DSeasonsRepository;
import com.umarbariev.sneakersshop.repository.dictionary.DSexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoeModelSpec {
    private static final String CATEGORY = "category";
    private static final String BRAND = "brand";
    private static final String SEASONS = "seasons";
    private static final String SEX = "sex";
    private static final String MODEL_NAME = "modelName";
    private static final String COST = "cost";
    private static final String IS_PREMIUM = "isPremium";
    private static final String IS_ADULT = "isAdult";

    private final DCategoryRepository categoryRepository;
    private final DSexRepository sexRepository;
    private final DBrandEntityRepository brandEntityRepository;
    private final DSeasonsRepository seasonsRepository;

    public Specification<ShoeModelEntity> filterBy(SearchCriteria searchCriteria) {
        return Specification
                   .where(hasCategory(searchCriteria.getCategory()))
                   .and(hasBrand(searchCriteria.getBrand()))
                   .and(hasSeason(searchCriteria.getSeason()))
                   .and(nameContains(searchCriteria.getNameContains()))
                   .and(hasSex(searchCriteria.getSex()))
                   .and(hasMinCost(searchCriteria.getMinCost()))
                   .and(hasMaxCost(searchCriteria.getMaxCost()))
                   .and(isPremium(searchCriteria.isPremium()))
                   .and(isAdult(searchCriteria.getAgeCategory()));
    }

    private Specification<ShoeModelEntity> hasCategory(String categoryCode) {
        return ((root, query, criteriaBuilder) -> categoryCode == null || categoryCode.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get(
            CATEGORY), categoryRepository.findByCode(categoryCode).orElseThrow(() -> new IllegalArgumentException("Нет категории с кодом " + categoryCode))));
    }

    private Specification<ShoeModelEntity> hasBrand(String brandCode) {
        return ((root, query, criteriaBuilder) -> brandCode == null || brandCode.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get(
            BRAND), brandEntityRepository.findByCode(brandCode).orElseThrow(() -> new IllegalArgumentException("Нет бренда с кодом " + brandCode))));
    }

    private Specification<ShoeModelEntity> hasSeason(String seasonCode) {
        return ((root, query, criteriaBuilder) -> seasonCode == null || seasonCode.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get(
            SEASONS), seasonsRepository.findByCode(seasonCode).orElseThrow(() -> new IllegalArgumentException("Нет сезона с кодом " + seasonCode))));
    }

    private Specification<ShoeModelEntity> hasSex(String sexCode) {
        return ((root, query, criteriaBuilder) -> sexCode == null || sexCode.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get(
            SEX), sexRepository.findByCode(sexCode).orElseThrow(() -> new IllegalArgumentException("Нет пола с кодом " + sexCode))));
    }


    private Specification<ShoeModelEntity> hasMinCost(Double minCost) {
        return ((root, query, criteriaBuilder) -> minCost == null || minCost == 0.0d ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThanOrEqualTo(root.get(
            COST), minCost));
    }

    private Specification<ShoeModelEntity> hasMaxCost(Double maxCost) {
        return ((root, query, criteriaBuilder) -> maxCost == null || maxCost == 0.0d ? criteriaBuilder.conjunction() : criteriaBuilder.le(root.get(
            COST), maxCost));
    }

    private Specification<ShoeModelEntity> nameContains(String nameContains) {
        return ((root, query, criteriaBuilder) -> nameContains == null || nameContains.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.like(root.get(
            MODEL_NAME), "%" + nameContains + "%"));
    }

    private Specification<ShoeModelEntity> isPremium(boolean premium) {
        return ((root, query, criteriaBuilder) -> premium ? criteriaBuilder.equal(root.get(IS_PREMIUM), true) : criteriaBuilder.conjunction());
    }

    private Specification<ShoeModelEntity> isAdult(String ageCategory) {
        if (ageCategory == null || ageCategory.equals("")) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        } else if(ageCategory.equals(SearchCriteria.ADULT)) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(IS_ADULT), true));
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(IS_ADULT), false));
    }
}
