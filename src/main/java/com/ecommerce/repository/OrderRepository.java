package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_IdOrderByIdDesc(Long customerId);

    List<Order> findByDeliveryPartner_IdOrderByIdDesc(Long deliveryPartnerId);

    List<Order> findByStatusOrderByIdDesc(OrderStatus status);
}
