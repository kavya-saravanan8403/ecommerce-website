package com.ecommerce.controller;

import com.ecommerce.dto.DeliveryPartnerOrderResponse;
import com.ecommerce.dto.DeliveryPartnerRequest;
import com.ecommerce.dto.DeliveryPartnerResponse;
import com.ecommerce.security.AdminOnly;
import com.ecommerce.service.DeliveryPartnerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-partners")
public class DeliveryPartnerController {

    private final DeliveryPartnerService deliveryPartnerService;

    public DeliveryPartnerController(DeliveryPartnerService deliveryPartnerService) {
        this.deliveryPartnerService = deliveryPartnerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AdminOnly
    public DeliveryPartnerResponse createPartner(@RequestBody DeliveryPartnerRequest request) {
        return deliveryPartnerService.createPartner(request);
    }

    @GetMapping("/{id}")
    public DeliveryPartnerResponse getPartnerById(@PathVariable("id") Long partnerId) {
        return deliveryPartnerService.getPartnerById(partnerId);
    }

    @PutMapping("/{id}")
    @AdminOnly
    public DeliveryPartnerResponse updatePartner(@PathVariable("id") Long partnerId,
                                                  @RequestBody DeliveryPartnerRequest request) {
        return deliveryPartnerService.updatePartner(partnerId, request);
    }

    @GetMapping("/{id}/orders")
    public List<DeliveryPartnerOrderResponse> getOrdersByPartner(@PathVariable("id") Long partnerId) {
        return deliveryPartnerService.getOrdersByPartner(partnerId);
    }
}