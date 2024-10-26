package com.project.CRMAPI.application.services;

import com.project.CRMAPI.application.ports.CustomerRepository;
import com.project.CRMAPI.domain.models.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
        return customerRepository.save(customer);
    }
}
