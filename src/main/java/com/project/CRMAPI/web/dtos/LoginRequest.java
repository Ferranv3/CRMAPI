package com.project.CRMAPI.web.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {
    private String email;
    private String password;
    private String userName;
}
