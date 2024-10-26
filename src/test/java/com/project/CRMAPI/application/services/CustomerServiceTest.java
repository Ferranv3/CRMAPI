package com.project.CRMAPI.application.services;

import com.project.CRMAPI.domain.models.Customer;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.application.ports.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private final CustomerService customerService = new CustomerService(customerRepository);

    @Test
    void testGetAllCustomers() {
        Customer customer = new Customer(UUID.randomUUID(), "John", "Doe", null, new User(), null);
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> customers = customerService.getAllCustomers();

        assertEquals(1, customers.size());
        assertEquals("John", customers.get(0).getName());
    }

    @Test
    void createCustomer_ShouldSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("userTest");
        customer.setSurname("testUser");

        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.createCustomer(customer);

        assertEquals("userTest", savedCustomer.getName());
        assertEquals("testUser", savedCustomer.getSurname());
        Mockito.verify(customerRepository, Mockito.times(1)).save(customer);
    }

    @Test
    void modifyPhoto_ShouldSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("userTest");
        customer.setSurname("testUser");

        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);
        Customer savedCustomer = customerService.createCustomer(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.of(savedCustomer));
        savedCustomer.setPhotoUrl("https://example.com/photos/usertest.jpg");
        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(savedCustomer);
        customerService.updatePhotoUrl(savedCustomer.getId(), "https://example.com/photos/usertest.jpg");


        assertEquals(customer.getId(), savedCustomer.getId());
        assertNotNull(savedCustomer.getPhotoUrl());
        Mockito.verify(customerRepository, Mockito.times(2)).save(customer);
    }
}
