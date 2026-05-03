package com.ecommerce.service;

import com.ecommerce.dto.DeliveryPartnerOrderResponse;
import com.ecommerce.dto.DeliveryPartnerRequest;
import com.ecommerce.dto.DeliveryPartnerResponse;
import com.ecommerce.entity.DeliveryPartner;
import com.ecommerce.repository.DeliveryPartnerRepository;
import com.ecommerce.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final OrderRepository orderRepository;

    public DeliveryPartnerService(DeliveryPartnerRepository deliveryPartnerRepository,
                                   OrderRepository orderRepository) {
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public DeliveryPartnerResponse createPartner(DeliveryPartnerRequest request) {
        if (Objects.isNull(request.getName()) || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delivery partner name is required");
        }
        DeliveryPartner partner = new DeliveryPartner();
        applyRequest(partner, request);
        return DeliveryPartnerResponse.fromEntity(deliveryPartnerRepository.save(partner));
    }

    @Transactional(readOnly = true)
    public DeliveryPartnerResponse getPartnerById(Long partnerId) {
        return DeliveryPartnerResponse.fromEntity(getPartnerOrThrow(partnerId));
    }

    @Transactional
    public DeliveryPartnerResponse updatePartner(Long partnerId, DeliveryPartnerRequest request) {
        DeliveryPartner partner = getPartnerOrThrow(partnerId);
        applyRequest(partner, request);
        return DeliveryPartnerResponse.fromEntity(deliveryPartnerRepository.save(partner));
    }

    @Transactional(readOnly = true)
    public List<DeliveryPartnerOrderResponse> getOrdersByPartner(Long partnerId) {
        getPartnerOrThrow(partnerId);
        return orderRepository.findByDeliveryPartner_IdOrderByIdDesc(partnerId)
                .stream()
                .map(DeliveryPartnerOrderResponse::fromEntity)
                .toList();
    }

    public DeliveryPartner getPartnerOrThrow(Long partnerId) {
        return deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Delivery partner not found for id: " + partnerId));
    }

    private void applyRequest(DeliveryPartner partner, DeliveryPartnerRequest request) {
        setIfNotNull(request.getName(), partner::setName);
        setIfNotNull(request.getAddress(), partner::setAddress);
        setIfNotNull(request.getPhoneNumber(), partner::setPhoneNumber);
    }

    private <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (Objects.nonNull(value)) {
            setter.accept(value);
        }
    }
}