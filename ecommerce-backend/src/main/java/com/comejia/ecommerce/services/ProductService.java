package com.comejia.ecommerce.services;

import com.comejia.ecommerce.models.dtos.requests.ProductRequestDto;
import com.comejia.ecommerce.models.dtos.responses.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto findById(Long id);

    List<ProductResponseDto> findAll();

    ProductResponseDto findByName(String name);

    ProductResponseDto save(ProductRequestDto productRequestDto);

    ProductResponseDto update(Long id, ProductRequestDto productRequestDto);

    void deleteById(Long id);
}
