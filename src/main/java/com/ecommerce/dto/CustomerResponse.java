package com.ecommerce.dto;

import com.ecommerce.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String address;
    private String address2;
    private String email;
    private String phone;

    public static CustomerResponse fromEntity(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getAddress2(),
                customer.getEmail(),
                customer.getPhone()
        );
    }
}
