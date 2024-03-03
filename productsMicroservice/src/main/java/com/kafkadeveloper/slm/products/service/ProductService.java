package com.kafkadeveloper.slm.products.service;

import com.kafkadeveloper.slm.products.dto.ProductsCreateRequest;

public interface ProductService {
    String createProduct(ProductsCreateRequest productsCreateRequest) throws Exception;
}
