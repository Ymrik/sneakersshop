package com.umarbariev.sneakersshop.service;

import com.umarbariev.sneakersshop.model.dto.*;
import com.umarbariev.sneakersshop.model.dto.dictionary.DOrderStatusDto;
import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import com.umarbariev.sneakersshop.model.entity.ClientEntity;
import com.umarbariev.sneakersshop.model.entity.OrderEntity;
import com.umarbariev.sneakersshop.model.entity.OrderShoesEntity;
import com.umarbariev.sneakersshop.model.entity.ShoesInStockEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.OrderStatusEntity;
import com.umarbariev.sneakersshop.model.entity.dictionary.ShoeModelEntity;
import com.umarbariev.sneakersshop.repository.OrderRepository;
import com.umarbariev.sneakersshop.repository.OrderShoesInStockRepository;
import com.umarbariev.sneakersshop.repository.ShoesInStockRepository;
import com.umarbariev.sneakersshop.repository.dictionary.DOrderStatusRepository;
import com.umarbariev.sneakersshop.repository.dictionary.DShoeModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final BucketCache bucketCache;
    private final DOrderStatusRepository orderStatusRepository;
    private final ShoesInStockRepository shoesInStockRepository;
    private final DShoeModelRepository shoeModelRepository;
    private final OrderShoesInStockRepository orderShoesInStockRepository;
    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ShoeModelService shoeModelService;

    @Transactional
    public void createOrder(CreateOrderDto createOrderDto) {
        if (createOrderDto.getBucket() == null) {
            fillBucket(createOrderDto);
        }

        OrderEntity orderEntity = new OrderEntity();
        OrderStatusEntity createdOrderStatus = orderStatusRepository.getByCode(DOrderStatusDto.CREATED_CODE)
                                                                    .orElseThrow(() -> new IllegalArgumentException(String.format(
                                                                        "Неверный код статуса: %s",
                                                                        DOrderStatusDto.CREATED_CODE)));
        orderEntity.setOrderStatus(createdOrderStatus);
        ClientEntity clientEntity = clientService.getClientEntity(createOrderDto.getUsername());
        orderEntity.setClientEntity(clientEntity);
        orderEntity.setDeliveryAddress(createOrderDto.getAddress());

        orderEntity = orderRepository.save(orderEntity);

        for(Map.Entry<DShoeModelDto, Long> entry : createOrderDto.getBucket().getShoes().entrySet()) {
            DShoeModelDto shoeModelDto = entry.getKey();
            Long count = entry.getValue();

            Map<ShoesInStockEntity, Long> countByShoeInStock = getCountByShoeInStock(shoeModelDto, count);
            for(Map.Entry<ShoesInStockEntity, Long> shoeInStock : countByShoeInStock.entrySet()) {
                OrderShoesEntity orderShoesEntity = new OrderShoesEntity();
                orderShoesEntity.setOrder(orderEntity);
                orderShoesEntity.setShoes(shoeInStock.getKey());
                orderShoesEntity.setCount(shoeInStock.getValue());
                orderShoesInStockRepository.save(orderShoesEntity);
            }
        }

    }

    private Map<ShoesInStockEntity, Long> getCountByShoeInStock(DShoeModelDto shoeModelDto, Long count) {
        Long shoeModelId = shoeModelDto.getId();
        ShoeModelEntity shoeModelEntity = shoeModelRepository.getById(shoeModelId);
        List<ShoesInStockEntity> entities = shoesInStockRepository.getAllByShoeModelEntity(shoeModelEntity);
        Map<ShoesInStockEntity, Long> resultMap = new HashMap<>();
        Long leftCount = count;

        for (ShoesInStockEntity entity : entities) {
            if (entity.getCount() >= leftCount) {
                resultMap.put(entity, leftCount);
                subtractCountFromShoeInStock(entity, leftCount);
                return resultMap;
            }
            resultMap.put(entity, entity.getCount());
            subtractCountFromShoeInStock(entity, entity.getCount());
            leftCount -= entity.getCount();
        }

        return resultMap;
    }

    public List<ClientOrdersStatusDto> getClientOrders(String username) {
        List<OrderEntity> orders = orderRepository.getAllByClientEntity_User_Username(username);
        return getOrdersDtoFromOrderEntities(orders);
    }

    public List<ClientOrdersStatusDto> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        return getOrdersDtoFromOrderEntities(orders);
    }

    private List<ClientOrdersStatusDto> getOrdersDtoFromOrderEntities(List<OrderEntity> orders) {
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        List<ClientOrdersStatusDto> resultList = new ArrayList<>();

        for (OrderEntity order : orders) {
            ClientOrdersStatusDto clientOrdersStatusDto = new ClientOrdersStatusDto();
            clientOrdersStatusDto.setClientUsername(order.getClientEntity().getUser().getUsername());
            clientOrdersStatusDto.setOrderId(order.getId());
            clientOrdersStatusDto.setDeliveryAddress(order.getDeliveryAddress());
            clientOrdersStatusDto.setOrderStatus(order.getOrderStatus().getName());
            List<OrderShoesDto> orderShoesDto = new ArrayList<>();

            List<OrderShoesEntity> orderShoes = orderShoesInStockRepository.getAllByOrder_Id(order.getId());
            Map<Long, List<OrderShoesEntity>> orderShoesEntityByShoeModelId = orderShoes.stream()
                    .collect(Collectors.groupingBy(
                            orderShoesEntity -> orderShoesEntity.getShoes()
                                    .getShoeModelEntity()
                                    .getId()));
            for (Map.Entry<Long, List<OrderShoesEntity>> entry : orderShoesEntityByShoeModelId.entrySet()) {
                DShoeModelDto shoeModelDto = shoeModelService.getShoeById(entry.getKey());
                Long shoesCount = 0L;
                for (OrderShoesEntity shoesEntity : entry.getValue()) {
                    shoesCount += shoesEntity.getCount();
                }
                orderShoesDto.add(new OrderShoesDto(shoeModelDto, shoesCount));
            }
            clientOrdersStatusDto.setOrderShoes(orderShoesDto);
            resultList.add(clientOrdersStatusDto);
        }
        return resultList;
    }

    public List<DOrderStatusDto> getAllStatuses() {
        return orderStatusRepository.findAll()
                .stream()
                .map(status -> new DOrderStatusDto(status.getId(), status.getCode(), status.getName(), status.getOrdinal()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateOrderStatus(UpdateOrderStatusDto updateOrderStatusDto) {
        Long orderId = updateOrderStatusDto.getOrderId();
        String newStatusCode = updateOrderStatusDto.getNewStatusCode();

        Optional<OrderEntity> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new IllegalArgumentException(String.format("Не существует заказа с ID=%s", orderId));
        }
        OrderEntity orderEntity = order.get();
        Optional<OrderStatusEntity> newOrderStatus = orderStatusRepository.getByCode(newStatusCode);
        if (newOrderStatus.isEmpty()) {
            throw new IllegalArgumentException(String.format("Не существует статуса с code=%s", newStatusCode));
        }
        orderEntity.setOrderStatus(newOrderStatus.get());
        orderRepository.save(orderEntity);
    }

    private void subtractCountFromShoeInStock(ShoesInStockEntity entity, Long count) {
        entity.setCount(entity.getCount() - count);
        shoesInStockRepository.save(entity);
    }

    public void fillBucket(CreateOrderDto createOrderDto) {
        String username = createOrderDto.getUsername();
        Bucket bucket = bucketCache.getBucket(username);
        createOrderDto.setBucket(bucket);
    }
}
