package com.project.CRMAPI.web.controllers;

import com.project.CRMAPI.application.services.AuthenticationService;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.security.JwtService;
import com.project.CRMAPI.web.dtos.LoginRequest;
import com.project.CRMAPI.web.dtos.LoginResponse;

import com.project.CRMAPI.web.dtos.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody SignUpRequest signUpRequest) {
        logger.info("Request received: GET /auth/signup {}", signUpRequest);
        User registeredUser = authenticationService.signup(signUpRequest);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        logger.info("Request received: GET /auth/login {}", loginRequest);
        User authenticatedUser = authenticationService.authenticate(loginRequest);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.ok(loginResponse);
    }
}