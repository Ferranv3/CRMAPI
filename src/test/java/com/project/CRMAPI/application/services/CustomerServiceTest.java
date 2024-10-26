package com.project.CRMAPI.application.services;

import com.project.CRMAPI.application.ports.UserRepository;
import com.project.CRMAPI.domain.models.Customer;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.application.ports.CustomerRepository;
import com.project.CRMAPI.web.dtos.CustomerRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CustomerService customerService = new CustomerService(customerRepository, userRepository);

    @BeforeAll
    static void setup() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("userTest");
        SecurityContextHolder.setContext(securityContext);
    }

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
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(mock(User.class)));
        customerService.updatePhotoUrl(savedCustomer.getId(), "https://example.com/photos/usertest.jpg");

        assertEquals(customer.getId(), savedCustomer.getId());
        assertNotNull(savedCustomer.getPhotoUrl());
        Mockito.verify(customerRepository, Mockito.times(2)).save(customer);
    }

    @Test
    void updateCustomer_ShouldSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("userTest");
        customer.setSurname("testUser");
        CustomerRequest customerRequest = mock(CustomerRequest.class);

        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);
        Customer savedCustomer = customerService.createCustomer(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.of(savedCustomer));
        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(savedCustomer);
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(mock(User.class)));
        customerService.updateCustomer(savedCustomer.getId(), customerRequest);

        assertEquals(customer.getId(), savedCustomer.getId());
        assertEquals(savedCustomer.getName(), customerRequest.getName());
        assertEquals(savedCustomer.getSurname(), customerRequest.getSurname());
        Mockito.verify(customerRepository, Mockito.times(2)).save(customer);
    }
}
