package com.umarbariev.sneakersshop.service;

import com.umarbariev.sneakersshop.mapper.ShoeModelMapper;
import com.umarbariev.sneakersshop.model.dto.EditShoeDto;
import com.umarbariev.sneakersshop.model.dto.SearchCriteria;
import com.umarbariev.sneakersshop.model.dto.ShoeModelInStockInfo;
import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import com.umarbariev.sneakersshop.model.entity.ShoesInStockEntity;
import com.umarbariev.sneakersshop.model.entity.StockroomEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.*;
import com.umarbariev.sneakersshop.repository.OrderRepository;
import com.umarbariev.sneakersshop.repository.OrderShoesInStockRepository;
import com.umarbariev.sneakersshop.repository.ShoesInStockRepository;
import com.umarbariev.sneakersshop.repository.StockroomRepository;
import com.umarbariev.sneakersshop.repository.dictionary.*;
import com.umarbariev.sneakersshop.util.ShoeModelSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoeModelService {

    private final DShoeModelRepository shoeModelRepository;
    private final ShoesInStockRepository shoesInStockRepository;
    private final DCategoryRepository categoryRepository;
    private final DBrandEntityRepository dBrandEntityRepository;
    private final OrderShoesInStockRepository orderShoesInStockRepository;
    private final OrderRepository orderRepository;
    private final DSexRepository dSexRepository;
    private final DSeasonsRepository seasonsRepository;
    private final StockroomRepository stockroomRepository;
    private final ShoeModelMapper shoeModelMapper;
    private final ShoeModelSpec shoeModelSpec;

    public List<DShoeModelDto> getAllShoes() {
        return shoeModelRepository.findAll().stream()
                                  .map(shoeModelMapper::fromEntity)
                                  .collect(Collectors.toList());
    }

    public List<DShoeModelDto> findShoes(SearchCriteria searchCriteria) {
        return shoeModelRepository.findAll(shoeModelSpec.filterBy(searchCriteria)).stream()
                                  .map(shoeModelMapper::fromEntity)
                                  .collect(Collectors.toList());
    }

    public DShoeModelDto getShoeById(Long id) {
        return shoeModelMapper.fromEntity(shoeModelRepository.findById(id)
                                                             .orElseThrow(() -> new IllegalArgumentException(String.format(
                                                                 "Не найдено обуви с id = %s",
                                                                 id))));
    }

    public List<ShoeModelInStockInfo> getShoeModelInStockInfos() {
        List<DShoeModelDto> shoeModelDtos = getAllShoes();
        List<ShoeModelInStockInfo> resultList = new ArrayList<>();

        shoeModelDtos.forEach(shoeModelDto -> {
            List<ShoesInStockEntity> shoesInStock = shoesInStockRepository.getAllByShoeModelEntityId(shoeModelDto.getId());
            if (shoesInStock.isEmpty()) {
                resultList.add(new ShoeModelInStockInfo(shoeModelDto, new HashMap<>()));
                return;
            }
            Map<String, Long> countByStock = shoesInStock.stream().collect(Collectors.toMap((ShoesInStockEntity entity) -> entity.getStockroomEntity().getAddress(), ShoesInStockEntity::getCount));
            if (countByStock.values().stream().allMatch(x->x.equals(0L))) {
                resultList.add(new ShoeModelInStockInfo(shoeModelDto, new HashMap<>()));
            } else{
                resultList.add(new ShoeModelInStockInfo(shoeModelDto, countByStock));
            }
        });
        return resultList;
    }

    @Transactional
    public void deleteShoe(Long shoeId) {
        if (orderShoesInStockRepository.existActiveOrdersWithGivenShoeId(shoeId)) {
            throw new IllegalArgumentException(String.format("Невозможно удалить обувь с id=%s, так есть незавершенные заказы", shoeId));
        }

        List<Long> shoesInStockIds = shoesInStockRepository.getIdsWithGivenShoeId(shoeId);
        shoesInStockRepository.deleteAllByIds(shoesInStockIds);

        List<Long> orderIds = orderShoesInStockRepository.getAllOrderIdsByShoesInStockIds(shoesInStockIds);
        orderShoesInStockRepository.deleteAllByShoeInStockIds(shoesInStockIds);

        orderRepository.deleteAllByIds(orderIds);

        shoeModelRepository.deleteById(shoeId);
    }

    @Transactional
    public void saveOrUpdateShoe(EditShoeDto editShoeDto) {
        if (editShoeDto.getId() == null) {
            saveNewShoe(editShoeDto);
        } else {
            updateShoe(editShoeDto);
        }
    }

    private void saveNewShoe(EditShoeDto editShoeDto) {
        ShoeModelEntity shoeModelEntity = new ShoeModelEntity();
        fillCommonFields(shoeModelEntity, editShoeDto);
        shoeModelEntity = shoeModelRepository.save(shoeModelEntity);
        updateStockInfo(shoeModelEntity, editShoeDto.getCountInFirstStock(), editShoeDto.getCountInSecondStock());
    }

    private void updateShoe(EditShoeDto editShoeDto) {
        ShoeModelEntity shoeModelEntity = shoeModelRepository.getById(editShoeDto.getId());
        fillCommonFields(shoeModelEntity, editShoeDto);
        shoeModelEntity = shoeModelRepository.save(shoeModelEntity);
        updateStockInfo(shoeModelEntity, editShoeDto.getCountInFirstStock(), editShoeDto.getCountInSecondStock());
    }

    private void updateStockInfo(ShoeModelEntity entity, Long firstStockCount, Long secondStockCount) {
        List<ShoesInStockEntity> shoesInStockEntities = shoesInStockRepository.getAllByShoeModelEntity(entity);
        // сохраняем на первый склад
        if (firstStockCount != null) {
            Optional<ShoesInStockEntity> firstStockOpt = shoesInStockEntities.stream().filter(stock -> stock.getId() == 1).findFirst();
            StockroomEntity firstStockroom = stockroomRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException(String.format("Нет склада с id=%s", 1L)));
            updateStockInfo(firstStockOpt, entity, firstStockroom, firstStockCount);
        }
        // сохраняем на второй склад
        if (secondStockCount != null) {
            Optional<ShoesInStockEntity> secondStockOpt = shoesInStockEntities.stream().filter(stock -> stock.getId() == 2).findFirst();
            StockroomEntity secondStockroom = stockroomRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException(String.format("Нет склада с id=%s", 2L)));
            updateStockInfo(secondStockOpt, entity, secondStockroom, secondStockCount);
        }
    }

    private void updateStockInfo(Optional<ShoesInStockEntity> stockOpt, ShoeModelEntity entity, StockroomEntity stockroom, Long count) {
        ShoesInStockEntity stock;
        if (stockOpt.isEmpty()) {
            stock = new ShoesInStockEntity();
            stock.setShoeModelEntity(entity);
            stock.setStockroomEntity(stockroom);
        } else {
            stock = stockOpt.get();
        }
        stock.setCount(count);
        shoesInStockRepository.save(stock);
    }

    private void fillCommonFields(ShoeModelEntity entity, EditShoeDto shoeDto) {
        Optional<CategoryEntity> categoryOpt = categoryRepository.findByCode(shoeDto.getCategoryCode());
        if (categoryOpt.isEmpty()) {
            throw new IllegalArgumentException(String.format("Нет категории с кодом %s", shoeDto.getCategoryCode()));
        }
        entity.setCategory(categoryOpt.get());
        Optional<BrandEntity> brandOpt = dBrandEntityRepository.findByCode(shoeDto.getBrandCode());
        if (brandOpt.isEmpty()) {
            throw new IllegalArgumentException(String.format("Нет бренда с кодом %s", shoeDto.getBrandCode()));
        }
        entity.setBrand(brandOpt.get());
        entity.setModelName(shoeDto.getModelName());
        Optional<SexEntity> sexOpt = dSexRepository.findByCode(shoeDto.getSexCode());
        if (sexOpt.isEmpty()) {
            throw new IllegalArgumentException(String.format("Нет пола с кодом %s", shoeDto.getSexCode()));
        }
        entity.setSex(sexOpt.get());
        entity.setCost(BigDecimal.valueOf(shoeDto.getCost()));
        entity.setIsPremium(shoeDto.isPremium());
        entity.setIsAdult(shoeDto.getIsAdult());
        entity.setDescription(shoeDto.getDescription());
        Optional<SeasonEntity> seasonsOpt = seasonsRepository.findByCode(shoeDto.getSeasonCode());
        if (seasonsOpt.isEmpty()) {
            throw new IllegalArgumentException(String.format("Нет сезона с кодом %s", shoeDto.getSeasonCode()));
        }
        entity.setSeasons(seasonsOpt.get());
        entity.setPhotoUrl(shoeDto.getPhotoUrl());
    }

    public EditShoeDto getEditShoeDto(Long shoeId) {
        ShoeModelEntity shoeModelEntity = shoeModelRepository.getById(shoeId);
        Long countInFirstStock = shoesInStockRepository.getCountInStock(shoeId, 1L);
        Long countInSecondStock = shoesInStockRepository.getCountInStock(shoeId, 2L);

        return EditShoeDto.builder()
                .id(shoeModelEntity.getId())
                .modelName(shoeModelEntity.getModelName())
                .brandCode(shoeModelEntity.getBrand().getCode())
                .categoryCode(shoeModelEntity.getCategory().getCode())
                .sexCode(shoeModelEntity.getSex().getCode())
                .cost(shoeModelEntity.getCost().doubleValue())
                .isPremium(shoeModelEntity.getIsPremium())
                .isAdult(shoeModelEntity.getIsAdult())
                .seasonCode(shoeModelEntity.getSeasons().getCode())
                .photoUrl(shoeModelEntity.getPhotoUrl())
                .description(shoeModelEntity.getDescription())
                .countInFirstStock(countInFirstStock)
                .countInSecondStock(countInSecondStock).build();
    }
}
