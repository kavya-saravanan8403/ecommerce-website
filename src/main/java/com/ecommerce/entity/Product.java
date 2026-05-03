package com.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brandname", length = 1000)
    private String brandName;

    @Column(precision = 19, scale = 2)
    private BigDecimal discount;

    @Column(length = 1000)
    private String model;

    @Column(length = 1000)
    private String name;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

}
