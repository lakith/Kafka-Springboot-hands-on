package com.kafkadeveloper.slm.products.rest;

import com.kafkadeveloper.slm.products.dto.ErrorMessage;
import com.kafkadeveloper.slm.products.dto.ProductsCreateRequest;
import com.kafkadeveloper.slm.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProducts(@RequestBody ProductsCreateRequest createProductRequest) {
        String productId = null;
        try {
            productId = productService.createProduct(createProductRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(new Date(), e.getMessage(), "/products"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
