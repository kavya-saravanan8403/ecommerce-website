package com.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long customerId;
    private Long deliveryPartnerId;
    private List<OrderItemRequest> items;
}