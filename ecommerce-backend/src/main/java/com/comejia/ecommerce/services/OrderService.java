package com.comejia.ecommerce.services;

import com.comejia.ecommerce.models.dtos.requests.OrderRequestDto;
import com.comejia.ecommerce.models.dtos.responses.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto findById(Long id);

    List<OrderResponseDto> findAll();

    OrderResponseDto save(OrderRequestDto orderRequestDto);

    OrderResponseDto update(Long id, OrderRequestDto orderRequestDto);

    void deleteById(Long id);
}
