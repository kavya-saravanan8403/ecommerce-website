package com.ecommerce.dto;

import com.ecommerce.entity.DeliveryPartner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;

    public static DeliveryPartnerResponse fromEntity(DeliveryPartner partner) {
        return new DeliveryPartnerResponse(
                partner.getId(),
                partner.getName(),
                partner.getAddress(),
                partner.getPhoneNumber()
        );
    }
}