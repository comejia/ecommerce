package com.comejia.ecommerce.controllers;

import com.comejia.ecommerce.models.dtos.requests.OrderRequestDto;
import com.comejia.ecommerce.models.dtos.responses.OrderResponseDto;
import com.comejia.ecommerce.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders() {
        log.info("REST: Fetching orders");
        return ResponseEntity.ok(this.orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        log.info("REST: Fetching order with ID: {}", id);
        return ResponseEntity.ok(this.orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        log.info("REST: Creating order");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.orderService.save(orderRequestDto));
    }

    // NOTA: la logica de actualización es complicado de verificar. Se deja para mas adelante.
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id,
                                                        @RequestBody OrderRequestDto orderRequestDto) {
        log.info("REST: Updating order with ID: {}", id);

        OrderResponseDto orderResponse = this.orderService.update(id, orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("REST: Deleting order with ID: {}", id);
        this.orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
