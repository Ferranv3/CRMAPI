package com.project.CRMAPI.web.controllers;

import com.project.CRMAPI.application.services.CustomerService;
import com.project.CRMAPI.application.services.UserService;
import com.project.CRMAPI.domain.models.Customer;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.web.dtos.CustomerPhotoUrlUpdateRequest;
import com.project.CRMAPI.web.dtos.CustomerRequest;
import com.project.CRMAPI.web.mappers.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;
    private final UserService userService;

    public CustomerController(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        logger.info("Request received: GET /customers");
        return customerService.getAllCustomers();
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequest customerRequest) {
        logger.info("Request received: POST /customers");
        User createdBy = userService.getUserById(UUID.fromString(customerRequest.getCreatedById())).orElseThrow();
        Customer customer = CustomerMapper.toDomainModel(customerRequest, createdBy);
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID customerId,
                                                   @RequestBody CustomerRequest customerRequest) {
        Customer updatedCustomer = customerService.updateCustomer(customerId, customerRequest);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PatchMapping("/{id}/photo-url")
    public ResponseEntity<Customer> updateCustomerPhotoUrl(@PathVariable UUID id,
                                                           @RequestBody CustomerPhotoUrlUpdateRequest request) {
        Customer updatedCustomer = customerService.updatePhotoUrl(id, request.getPhotoUrl());
        return ResponseEntity.ok(updatedCustomer);
    }
}
