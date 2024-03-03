package com.kafkadeveloper.slm.products.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductsCreateRequest {
    private @NonNull  String title;
    private @NonNull BigDecimal price;
    private @NonNull Integer quantity;
}
