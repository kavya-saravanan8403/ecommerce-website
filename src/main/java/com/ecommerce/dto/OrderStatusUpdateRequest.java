package com.ecommerce.dto;

import com.ecommerce.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateRequest {
    private OrderStatus status;
    private String currentLocation;
}