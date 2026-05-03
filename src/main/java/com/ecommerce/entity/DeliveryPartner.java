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
@Table(name = "deliverypartner")
@Getter
@Setter
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String address;

    @Column(length = 1000)
    private String name;

    @Column(name = "phonenumber", length = 10)
    private String phoneNumber;

    @OneToMany(mappedBy = "deliveryPartner")
    private List<Order> orders = new ArrayList<>();
}
