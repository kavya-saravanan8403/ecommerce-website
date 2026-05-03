package com.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryPartnerRequest {
    private String name;
    private String address;
    private String phoneNumber;
}