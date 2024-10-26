package com.project.CRMAPI.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.CRMAPI.domain.models.Customer;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.infrastructure.repositories.JpaCustomerRepository;
import com.project.CRMAPI.infrastructure.repositories.JpaUserRepository;
import com.project.CRMAPI.web.dtos.CustomerPhotoUrlUpdateRequest;
import com.project.CRMAPI.web.dtos.CustomerRequest;
import com.project.CRMAPI.web.dtos.LoginRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaCustomerRepository customerRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String username = "testuser";
    private User userCreated;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    static void tearDown(@Autowired JpaCustomerRepository customerRepository,
                         @Autowired JpaUserRepository userRepository) {
        customerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        customerRepository.deleteAll();
    }

    @Test
    void createCustomer_ShouldBeForbbiden() throws Exception {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name(username)
                .surname(username)
                .photoUrl("https://example.com/photos/usertest.jpg")
                .build();

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() throws Exception {
        String token = this.createUserAndLogin();

        CustomerRequest customerRequest = CustomerRequest.builder()
                .name(username)
                .surname(username)
                .createdById(String.valueOf(userCreated.getId()))
                .build();

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCustomerPhotoUrl_ShouldUpdatePhotoUrl() throws Exception {
        Customer customer = new Customer();
        String token = this.createUserAndLogin();

        customer.setName(username);
        customer.setSurname(username);
        customer.setCreatedBy(userCreated);
        customer.setLastModifiedBy(userCreated);

        customer = customerRepository.save(customer);

        CustomerPhotoUrlUpdateRequest updateRequest = new CustomerPhotoUrlUpdateRequest();
        updateRequest.setPhotoUrl("https://example.com/updated-photo.jpg");

        mockMvc.perform(patch("/customers/" + customer.getId() + "/photo-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    private String createUserAndLogin() throws Exception {
        String password = "testPassword";
        String email = "email@email.com";
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .role("USER")
                .build();

        userCreated = userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .userName(username)
                .password(password)
                .email(email)
                .build();

        // get JWT from user logged
        String tokenResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(tokenResponse).get("token").asText();
    }
}
