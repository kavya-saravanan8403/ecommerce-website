package com.ecommerce.service;

import com.ecommerce.dto.FeeBreakdownResponse;
import com.ecommerce.dto.OrderItemResponse;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.OrderResponse;
import com.ecommerce.dto.OrderStatusUpdateRequest;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.DeliveryPartner;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderService {

    private static final Set<OrderStatus> TERMINAL_STATUSES = Set.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryPartnerService deliveryPartnerService;
    private final FeeCalculationService feeCalculationService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CustomerRepository customerRepository,
                        DeliveryPartnerService deliveryPartnerService,
                        FeeCalculationService feeCalculationService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.deliveryPartnerService = deliveryPartnerService;
        this.feeCalculationService = feeCalculationService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        if (Objects.isNull(request.getItems()) || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must have at least one item");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer not found for id: " + request.getCustomerId()));

        DeliveryPartner partner = deliveryPartnerService.getPartnerOrThrow(request.getDeliveryPartnerId());

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryPartner(partner);
        order.setStatus(OrderStatus.CREATED);
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> items = request.getItems().stream()
                .map(itemReq -> feeCalculationService.buildOrderItem(savedOrder.getId(), itemReq))
                .toList();
        orderItemRepository.saveAll(items);

        List<OrderItemResponse> itemResponses = orderItemRepository
                .findByOrderIdWithProduct(savedOrder.getId())
                .stream()
                .map(OrderItemResponse::fromEntity)
                .toList();

        return OrderResponse.fromEntity(savedOrder, itemResponses);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = getOrderOrThrow(orderId);
        List<OrderItemResponse> items = orderItemRepository.findByOrderIdWithProduct(orderId)
                .stream()
                .map(OrderItemResponse::fromEntity)
                .toList();
        return OrderResponse.fromEntity(order, items);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(OrderStatus status) {
        List<Order> orders = Objects.nonNull(status)
                ? orderRepository.findByStatusOrderByIdDesc(status)
                : orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    List<OrderItemResponse> items = orderItemRepository
                            .findByOrderIdWithProduct(order.getId())
                            .stream()
                            .map(OrderItemResponse::fromEntity)
                            .toList();
                    return OrderResponse.fromEntity(order, items);
                })
                .toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = getOrderOrThrow(orderId);
        validateTransition(order.getStatus(), request.getStatus());
        order.setStatus(request.getStatus());
        if (Objects.nonNull(request.getCurrentLocation())) {
            order.setCurrentLocation(request.getCurrentLocation());
        }
        Order updated = orderRepository.save(order);
        List<OrderItemResponse> items = orderItemRepository.findByOrderIdWithProduct(orderId)
                .stream()
                .map(OrderItemResponse::fromEntity)
                .toList();
        return OrderResponse.fromEntity(updated, items);
    }

    @Transactional(readOnly = true)
    public FeeBreakdownResponse getFeeBreakdown(Long orderId) {
        getOrderOrThrow(orderId);
        List<OrderItemResponse> items = orderItemRepository.findByOrderIdWithProduct(orderId)
                .stream()
                .map(OrderItemResponse::fromEntity)
                .toList();

        FeeBreakdownResponse response = new FeeBreakdownResponse();
        response.setOrderId(orderId);
        response.setItems(items);
        response.setTotalDiscount(sum(items.stream().map(OrderItemResponse::getDiscount).toList()));
        response.setTotalTaxes(sum(items.stream().map(OrderItemResponse::getTaxes).toList()));
        response.setTotalPlatformCharge(sum(items.stream().map(OrderItemResponse::getPlatformCharge).toList()));
        response.setTotalConvenienceFee(sum(items.stream().map(OrderItemResponse::getConvenienceFee).toList()));
        response.setGrandTotal(sum(items.stream().map(OrderItemResponse::getTotalAmount).toList()));
        return response;
    }

    private Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found for id: " + orderId));
    }

    private void validateTransition(OrderStatus current, OrderStatus next) {
        if (Objects.isNull(next)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target status is required");
        }
        if (TERMINAL_STATUSES.contains(current)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot transition from terminal status: " + current.getDisplay());
        }
        if (next == OrderStatus.CANCELLED) {
            return;
        }
        if (next.getCode() != current.getCode() + 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Invalid transition from " + current.getDisplay() + " to " + next.getDisplay());
        }
    }

    private BigDecimal sum(List<BigDecimal> values) {
        return values.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}