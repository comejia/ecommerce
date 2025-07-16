package com.comejia.ecommerce.services;

import com.comejia.ecommerce.exceptions.ProductNotFoundException;
import com.comejia.ecommerce.models.dtos.requests.ProductRequestDto;
import com.comejia.ecommerce.models.dtos.responses.ProductResponseDto;
import com.comejia.ecommerce.models.entities.Product;
import com.comejia.ecommerce.models.mappers.ProductMapper;
import com.comejia.ecommerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponseDto findById(Long id) {
        return this.productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductResponseDto> findAll() {
        return this.productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponseDto findByName(String name) {
        return this.productRepository.findByName(name)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + name));
    }

    @Transactional
    @Override
    public ProductResponseDto save(ProductRequestDto productRequestDto) {
        Product product = productMapper.toEntity(productRequestDto);
        Product savedProduct = this.productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    @Override
    public ProductResponseDto update(Long id, ProductRequestDto productRequestDto) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        return productOptional.map(product -> {
                    product.setName(productRequestDto.getName());
                    product.setPrice(productRequestDto.getPrice());
                    product.setStock(productRequestDto.getStock());
                    Product updatedProduct = this.productRepository.save(product);
                    return productMapper.toDto(updatedProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!this.productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        this.productRepository.deleteById(id);
    }
}
