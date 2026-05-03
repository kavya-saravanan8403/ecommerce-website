package com.ecommerce.dto;

import com.ecommerce.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long deliveryPartnerId;
    private String deliveryPartnerName;
    private String status;
    private String currentLocation;
    private List<OrderItemResponse> items;

    public static OrderResponse fromEntity(Order order, List<OrderItemResponse> items) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        if (Objects.nonNull(order.getCustomer())) {
            response.setCustomerId(order.getCustomer().getId());
            response.setCustomerName(order.getCustomer().getName());
        }
        if (Objects.nonNull(order.getDeliveryPartner())) {
            response.setDeliveryPartnerId(order.getDeliveryPartner().getId());
            response.setDeliveryPartnerName(order.getDeliveryPartner().getName());
        }
        if (Objects.nonNull(order.getStatus())) {
            response.setStatus(order.getStatus().getDisplay());
        }
        response.setCurrentLocation(order.getCurrentLocation());
        response.setItems(items);
        return response;
    }
}