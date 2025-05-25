package com.eminyagiz.creditModule.service;

import com.eminyagiz.creditModule.model.entity.Customer;
import com.eminyagiz.creditModule.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        if(Objects.isNull(customer)) throw new IllegalArgumentException("Customer is null");
        return customerRepository.save(customer);
    }

    public boolean hasEnoughLimit(String customerName, BigDecimal loanAmount) {
        Optional<Customer> customerOpt = customerRepository.findByName(customerName);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            return customer.getCreditLimit().compareTo(loanAmount) >= 0;
        }
        return false;
    }

}