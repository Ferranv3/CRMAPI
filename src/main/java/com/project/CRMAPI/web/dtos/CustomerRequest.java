package com.project.CRMAPI.web.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequest {
    private String name;
    private String surname;
    private String photoUrl;
    private String createdById;
}
