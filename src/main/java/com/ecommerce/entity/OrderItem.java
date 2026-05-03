package com.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "orderitems")
@IdClass(OrderItemId.class)
@Getter
@Setter
public class OrderItem {

    @Id
    @Column(name = "orderid")
    private Long orderId;

    @Id
    @Column(name = "productid")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "orderid",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "productid",
            nullable = false,
            insertable = false,
            updatable = false    )
    private Product product;

    @Column(name = "quantity")
    private Short quantity;

    @Column(name = "unitprice", precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount", precision = 19, scale = 2)
    private BigDecimal discount;

    @Column(name = "taxes", precision = 19, scale = 2)
    private BigDecimal taxes;

    @Column(name = "taxrate", precision = 19, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "platformcharge", precision = 19, scale = 2)
    private BigDecimal platformCharge;

    @Column(name = "conveniencefee", precision = 19, scale = 2)
    private BigDecimal convenienceFee;

    @Column(name = "totalfee", precision = 19, scale = 2)
    private BigDecimal totalFee;

    @Column(name = "totalamount", precision = 19, scale = 2)
    private BigDecimal totalAmount;
}
