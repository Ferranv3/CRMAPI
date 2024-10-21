package com.project.CRMAPI.application.ports;

import com.project.CRMAPI.domain.models.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
    Customer save(Customer customer);
}
