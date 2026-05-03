package com.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String brandName;
    private String model;
    private BigDecimal price;
    private BigDecimal discount;
}