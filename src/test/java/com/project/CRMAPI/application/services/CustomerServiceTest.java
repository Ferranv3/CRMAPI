package com.project.CRMAPI.application.services;

import com.project.CRMAPI.domain.models.Customer;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.application.ports.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    @Test
    void testGetAllCustomers() {
        CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
        CustomerService customerService = new CustomerService(customerRepository);

        Customer customer = new Customer(UUID.randomUUID(), "John", "Doe", null, new User(), null);
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> customers = customerService.getAllCustomers();

        assertEquals(1, customers.size());
        assertEquals("John", customers.get(0).getName());
    }
}