package com.comejia.ecommerce.controllers;

import com.comejia.ecommerce.models.dtos.requests.ProductRequestDto;
import com.comejia.ecommerce.models.dtos.responses.ProductResponseDto;
import com.comejia.ecommerce.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts() {
        log.info("REST: Fetching products");
        return ResponseEntity.ok(this.productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        log.info("REST: Fetching product with ID: {}", id);
        return ResponseEntity.ok(this.productService.findById(id));
    }

    @GetMapping(params = "name")
    public ResponseEntity<ProductResponseDto> getProductByName(@RequestParam String name) {
        log.info("REST: Fetching product with name: {}", name);
        return ResponseEntity.ok(this.productService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequest) {
        log.info("REST: Creating product");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.productService.save(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
                                                            @RequestBody ProductRequestDto productRequest) {
        log.info("REST: Updating product with ID: {}", id);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.productService.update(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("REST: Deleting product with ID: {}", id);

        this.productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
