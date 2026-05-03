package com.ecommerce.service;

import com.ecommerce.dto.OrderItemRequest;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FeeCalculationService {

    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.18);
    private static final BigDecimal PLATFORM_RATE = BigDecimal.valueOf(0.02);
    private static final BigDecimal CONVENIENCE_FEE = BigDecimal.valueOf(10.00);

	private final ProductService productService;

	public FeeCalculationService(ProductService productService) {
		this.productService = productService;
	}
    public OrderItem buildOrderItem(Long orderId, OrderItemRequest itemReq) {
		Product product = productService.getProductOrThrow(itemReq.getProductId());

		OrderItem item = new OrderItem();
        item.setOrderId(orderId);
        item.setProductId(product.getId());
        item.setQuantity(itemReq.getQuantity());

        BigDecimal unitPrice = product.getPrice();
        BigDecimal discount = product.getDiscount() != null ? product.getDiscount() : BigDecimal.ZERO;
        BigDecimal qty = BigDecimal.valueOf(itemReq.getQuantity());
        BigDecimal baseAmount = unitPrice.multiply(qty);

        BigDecimal taxes = baseAmount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal platformCharge = baseAmount.multiply(PLATFORM_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalFee = taxes.add(platformCharge).add(CONVENIENCE_FEE);
        BigDecimal totalAmount = baseAmount.subtract(discount).add(totalFee);

        item.setUnitPrice(unitPrice);
        item.setDiscount(discount);
        item.setTaxRate(TAX_RATE);
        item.setTaxes(taxes);
        item.setPlatformCharge(platformCharge);
        item.setConvenienceFee(CONVENIENCE_FEE);
        item.setTotalFee(totalFee);
        item.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));

        return item;
    }
}