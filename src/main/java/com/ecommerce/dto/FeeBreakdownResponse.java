package com.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class FeeBreakdownResponse {
    private Long orderId;
    private List<OrderItemResponse> items;
    private BigDecimal totalDiscount;
    private BigDecimal totalTaxes;
    private BigDecimal totalPlatformCharge;
    private BigDecimal totalConvenienceFee;
    private BigDecimal grandTotal;
}