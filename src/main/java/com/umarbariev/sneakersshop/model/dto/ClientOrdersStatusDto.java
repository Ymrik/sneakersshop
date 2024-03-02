package com.umarbariev.sneakersshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrdersStatusDto {
    private String clientUsername;
    private Long orderId;
    private List<OrderShoesDto> orderShoes;
    private String orderStatus;
    private String deliveryAddress;
}
