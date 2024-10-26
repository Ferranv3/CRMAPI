package com.project.CRMAPI.application.ports;

import com.project.CRMAPI.domain.models.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    List<Customer> findAll();
    Optional<Customer> findById(UUID id);
    Customer save(Customer customer);
}
