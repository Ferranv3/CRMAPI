package com.project.CRMAPI.web.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ExceptionResponse {
    private HttpStatus httpCode;
    private String message;
}
