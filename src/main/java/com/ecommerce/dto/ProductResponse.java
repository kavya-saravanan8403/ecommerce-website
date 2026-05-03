package com.ecommerce.dto;

import com.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String brandName;
    private String model;
    private BigDecimal price;
    private BigDecimal discount;

    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getBrandName(),
                product.getModel(),
                product.getPrice(),
                product.getDiscount()
        );
    }
}