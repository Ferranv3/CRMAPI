package com.project.CRMAPI.web.controllers;

import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.infrastructure.repositories.JpaUserRepository;
import com.project.CRMAPI.web.dtos.LoginRequest;
import com.project.CRMAPI.web.dtos.SignUpRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private final String username = "testuser";
    private final String password = "testPassword";
    private final String email = "email@email.com";

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void testSignUpUser_Success() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userName(username)
                .password(password)
                .email(email)
                .build();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        this.createTestUser();

        LoginRequest loginRequest = LoginRequest.builder()
                .userName(username)
                .password(password)
                .email(email)
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() throws Exception {
        this.createTestUser();

        LoginRequest loginRequest = LoginRequest.builder()
                .userName(username)
                .password("password")
                .email(email)
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessProtectedEndpointWithJwt() throws Exception {
        this.createTestUser();

        LoginRequest loginRequest = LoginRequest.builder()
                .userName(username)
                .password(password)
                .email(email)
                .build();

        String token = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String jwtToken = objectMapper.readTree(token).get("token").asText();

        mockMvc.perform(get("/customers")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void createTestUser() {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .isAdmin(false)
                .build();

        userRepository.save(user);
    }
}


