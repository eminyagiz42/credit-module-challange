package com.eminyagiz.creditModule.service;

import com.eminyagiz.creditModule.model.entity.Customer;
import com.eminyagiz.creditModule.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final CustomerService customerService = new CustomerService(customerRepository);


    @Test
    void shouldReturnCustomerWhenIdIsValid() {
        // Given
        Customer customer = Customer.builder()
                .id(1L)
                .build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // When
        Optional<Customer> result = customerService.getCustomerById(1L);

        // Then
        assertEquals(result.isPresent(),true);
        assertEquals(result.get().getId(),1L);
    }

    @Test
    void shouldReturnEmptyOptionalWhenCustomerDoesNotExist() {
        // Given
        when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // When
        Optional<Customer> result = customerService.getCustomerById(100L);

        // Then
        assertEquals(result.isEmpty(),true);
    }

    @Test
    void shouldSaveCustomerSuccessfully() {
        // Given
        Customer customer = Customer.builder()
                .id(1L)
                .build();
        when(customerRepository.save(customer)).thenReturn(customer);

        // When
        Customer result = customerService.saveCustomer(customer);

        // Then
        assertNotNull(result);
        assertEquals(result.getId(), customer.getId());
    }

    @Test
    void shouldThrowExceptionWhenSavingCustomerFails() {
        // Given
        Customer customer = Customer.builder()
                .id(1L)
                .build();
        when(customerRepository.save(customer)).thenThrow(new RuntimeException("Save failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.saveCustomer(customer));
        assertEquals("Save failed", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSavingNullCustomer() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomer(null));
        assertEquals("Customer is null", exception.getMessage());
    }
}