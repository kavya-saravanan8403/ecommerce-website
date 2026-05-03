package com.ecommerce.controller;

import com.ecommerce.dto.FeeBreakdownResponse;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.OrderResponse;
import com.ecommerce.dto.OrderStatusUpdateRequest;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.security.AdminOnly;
import com.ecommerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable("id") Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping
	@AdminOnly
    public List<OrderResponse> getAllOrders(@RequestParam(value = "status", required = false) OrderStatus status) {
        return orderService.getAllOrders(status);
    }

    @PatchMapping("/{id}/status")
    @AdminOnly
    public OrderResponse updateStatus(@PathVariable("id") Long orderId,
                                       @RequestBody OrderStatusUpdateRequest request) {
        return orderService.updateStatus(orderId, request);
    }

    @GetMapping("/{id}/fees")
    public FeeBreakdownResponse getFeeBreakdown(@PathVariable("id") Long orderId) {
        return orderService.getFeeBreakdown(orderId);
    }
}