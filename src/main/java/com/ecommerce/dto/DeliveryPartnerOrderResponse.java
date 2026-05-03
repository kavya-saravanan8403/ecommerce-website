package com.ecommerce.dto;

import com.ecommerce.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class DeliveryPartnerOrderResponse {
    private Long orderId;
    private String customerName;
    private String status;
    private String currentLocation;

    public static DeliveryPartnerOrderResponse fromEntity(Order order) {
        DeliveryPartnerOrderResponse response = new DeliveryPartnerOrderResponse();
        response.setOrderId(order.getId());
        response.setCurrentLocation(order.getCurrentLocation());
        if (Objects.nonNull(order.getCustomer())) {
            response.setCustomerName(order.getCustomer().getName());
        }
        if (Objects.nonNull(order.getStatus())) {
            response.setStatus(order.getStatus().getDisplay());
        }
        return response;
    }
}