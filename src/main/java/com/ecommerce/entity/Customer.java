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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, updatable = false)
    private String name;

    @Column(length = 1000)
    private String address;

    @Column(name = "address2", length = 1000)
    private String address2;

    @Column(length = 40)
    private String email;

    @Column(length = 10)
    private String phone;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();
}
