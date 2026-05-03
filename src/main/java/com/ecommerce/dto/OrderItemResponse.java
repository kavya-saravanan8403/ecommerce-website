package com.ecommerce.dto;

import com.ecommerce.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private Short quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal taxRate;
    private BigDecimal taxes;
    private BigDecimal platformCharge;
    private BigDecimal convenienceFee;
    private BigDecimal totalFee;
    private BigDecimal totalAmount;

    public static OrderItemResponse fromEntity(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(item.getProductId());
        if (Objects.nonNull(item.getProduct())) {
            response.setProductName(item.getProduct().getName());
        }
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setDiscount(item.getDiscount());
        response.setTaxRate(item.getTaxRate());
        response.setTaxes(item.getTaxes());
        response.setPlatformCharge(item.getPlatformCharge());
        response.setConvenienceFee(item.getConvenienceFee());
        response.setTotalFee(item.getTotalFee());
        response.setTotalAmount(item.getTotalAmount());
        return response;
    }
}