package com.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderResponse {
    private Long id;
    private String currentLocation;
    private String statusDisplay;
    private Long deliveryPartnerId;
}
