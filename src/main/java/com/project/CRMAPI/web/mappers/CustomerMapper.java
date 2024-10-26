package com.project.CRMAPI.web.mappers;

import com.project.CRMAPI.domain.models.Customer;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.web.dtos.CustomerRequest;

public class CustomerMapper {
    public static Customer toDomainModel(CustomerRequest request, User createdBy) {
        return new Customer(
                null,
                request.getName(),
                request.getSurname(),
                request.getPhotoUrl(),
                createdBy,
                createdBy
        );
    }
}
