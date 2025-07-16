package com.comejia.ecommerce.services;

import com.comejia.ecommerce.exceptions.InsufficientStockException;
import com.comejia.ecommerce.exceptions.OrderNotFoundException;
import com.comejia.ecommerce.models.dtos.requests.OrderRequestDto;
import com.comejia.ecommerce.models.dtos.responses.OrderResponseDto;
import com.comejia.ecommerce.models.entities.Item;
import com.comejia.ecommerce.models.entities.Order;
import com.comejia.ecommerce.exceptions.ProductNotFoundException;
import com.comejia.ecommerce.models.mappers.OrderMapper;
import com.comejia.ecommerce.repositories.OrderRepository;
import com.comejia.ecommerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponseDto findById(Long id) {
        return this.orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> findAll() {
        return this.orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public OrderResponseDto save(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        orderRequestDto.getItems()
                .forEach(itemDto -> this.productRepository.findById(itemDto.getProductId())
                        .ifPresentOrElse(
                                product -> {
                                    if (!product.hasStock(itemDto.getQuantity())) {
                                        throw new InsufficientStockException(
                                                "Insufficient stock for product ID: " + itemDto.getProductId()
                                        );
                                    }
                                    order.addItem(new Item(product, itemDto.getQuantity()));
                                    product.reduceStock(itemDto.getQuantity());
                                },
                                () -> {
                                    throw new ProductNotFoundException("Product not found with ID: " + itemDto.getProductId());
                                }
                        )
                );

        Order savedOrder = this.orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    // NOTA: la logica de actualización es complicado de verificar. Se deja para mas adelante.
    @Override
    public OrderResponseDto update(Long id, OrderRequestDto orderRequestDto) {
        return this.orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!this.orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with ID: " + id);
        }
        this.orderRepository.deleteById(id);
    }
}
