package com.project.CRMAPI.infraestructure.repositories;

import com.project.CRMAPI.domain.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaCustomerRepository extends JpaRepository<Customer, UUID> {
}
