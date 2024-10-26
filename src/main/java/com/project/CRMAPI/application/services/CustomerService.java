package com.project.CRMAPI.application.services;

import com.project.CRMAPI.application.ports.CustomerRepository;
import com.project.CRMAPI.application.ports.UserRepository;
import com.project.CRMAPI.domain.models.Customer;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.web.dtos.CustomerRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updatePhotoUrl(UUID id, String photoUrl) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
        customer.setPhotoUrl(photoUrl);
        customer.setLastModifiedBy(getActualUser());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UUID id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
        customer.setName(customerRequest.getName());
        customer.setSurname(customerRequest.getSurname());
        customer.setPhotoUrl(customerRequest.getPhotoUrl());
        customer.setLastModifiedBy(getActualUser());
        return customerRepository.save(customer);
    }

    private User getActualUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
