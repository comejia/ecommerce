package com.comejia.ecommerce.models.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    private Long userId;
    private List<ItemRequestDto> items;
}
