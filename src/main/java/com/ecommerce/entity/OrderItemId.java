package com.ecommerce.entity;


import lombok.Getter;

import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter

public class OrderItemId implements Serializable {

    private Long orderId;

    private Long productId;
}
