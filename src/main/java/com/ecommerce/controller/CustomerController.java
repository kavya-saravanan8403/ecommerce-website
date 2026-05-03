package com.ecommerce.controller;

import com.ecommerce.dto.CustomerOrderResponse;
import com.ecommerce.dto.CustomerRequest;
import com.ecommerce.dto.CustomerResponse;
import com.ecommerce.dto.CustomerUpdateRequest;
import com.ecommerce.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public CustomerResponse createCustomer(@RequestBody CustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable("id") Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(@PathVariable("id") Long customerId, @RequestBody CustomerUpdateRequest request) {
        return customerService.updateCustomer(customerId, request);
    }

    @GetMapping("/{id}/orders")
    public List<CustomerOrderResponse> getCustomerOrders(@PathVariable("id") Long customerId) {
        return customerService.getCustomerOrders(customerId);
    }
}
