package com.project.CRMAPI.web.controllers;

import com.project.CRMAPI.domain.models.Customer;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.infrastructure.repositories.JpaCustomerRepository;
import com.project.CRMAPI.infrastructure.repositories.JpaUserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaCustomerRepository customerRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User(null, "admin", "passTest", "email@email.com", "ADMIN", true);
        user = userRepository.save(user);

        Customer customer = new Customer(null, "John", "Doe", null, user, null);
        customerRepository.save(customer);
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
}
