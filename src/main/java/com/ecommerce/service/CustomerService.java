package com.ecommerce.service;

import com.ecommerce.dto.CustomerOrderResponse;
import com.ecommerce.dto.CustomerRequest;
import com.ecommerce.dto.CustomerResponse;
import com.ecommerce.dto.CustomerUpdateRequest;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Order;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
public class CustomerService
{

	private final CustomerRepository customerRepository;
	private final OrderRepository orderRepository;

	public CustomerService(CustomerRepository customerRepository, OrderRepository orderRepository)
	{
		this.customerRepository = customerRepository;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public CustomerResponse createCustomer(CustomerRequest request)
	{
		Customer customer = new Customer();
		applyCreateRequest(customer, request);
		Customer savedCustomer = customerRepository.save(customer);
		return CustomerResponse.fromEntity(savedCustomer);
	}

	@Transactional(readOnly = true)
	public CustomerResponse getCustomerById(Long customerId)
	{
		Customer customer = getCustomerOrThrow(customerId);
		return CustomerResponse.fromEntity(customer);
	}

	@Transactional
	public CustomerResponse updateCustomer(Long customerId, CustomerUpdateRequest request)
	{
		Customer customer = getCustomerOrThrow(customerId);
		applyUpdateRequest(customer, request);
		Customer updatedCustomer = customerRepository.save(customer);
		return CustomerResponse.fromEntity(updatedCustomer);
	}

	@Transactional(readOnly = true)
	public List<CustomerOrderResponse> getCustomerOrders(Long customerId)
	{
		getCustomerOrThrow(customerId);
		return orderRepository.findByCustomer_IdOrderByIdDesc(customerId)
			.stream()
			.map(this::toCustomerOrderResponse)
			.toList();
	}

	private Customer getCustomerOrThrow(Long customerId)
	{
		return customerRepository.findById(customerId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"Customer not found for id: " + customerId
			));
	}

	private void applyCreateRequest(Customer customer, CustomerRequest request)
	{
		if(Objects.isNull(request.getName()) || request.getName().isBlank())
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name is required");
		}
		customer.setName(request.getName());
		customer.setAddress(request.getAddress());
		customer.setAddress2(request.getAddress2());
		customer.setEmail(request.getEmail());
		customer.setPhone(request.getPhone());
	}

	private void applyUpdateRequest(Customer customer, CustomerUpdateRequest request)
	{
		setIfNotNull(request.getAddress(), customer::setAddress);
		setIfNotNull(request.getAddress2(), customer::setAddress2);
		setIfNotNull(request.getEmail(), customer::setEmail);
		setIfNotNull(request.getPhone(), customer::setPhone);
	}

	private <T> void setIfNotNull(T value, Consumer<T> setter)
	{
		if(Objects.nonNull(value))
		{
			setter.accept(value);
		}
	}

	private CustomerOrderResponse toCustomerOrderResponse(Order order)
	{
		CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();
		customerOrderResponse.setId(order.getId());
		customerOrderResponse.setCurrentLocation(order.getCurrentLocation());
		if(Objects.nonNull(order.getStatus()))
		{
			customerOrderResponse.setStatusDisplay(order.getStatus().getDisplay());
		}

		return customerOrderResponse;
	}
}
