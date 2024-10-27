package com.project.CRMAPI.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.CRMAPI.domain.models.User;
import com.project.CRMAPI.infrastructure.repositories.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaUserRepository userRepository;

    private User existingUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        existingUser = userRepository.save(new User(null, "admin", "passTest", "email@email.com", true));
    }

    @Test
    void testGetAllUsers_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin")
    void testGetAllUsers_ShouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUser_ShouldUpdateUserDetails() throws Exception {
        existingUser.setUsername("updatedUser");
        existingUser.setEmail("updatedUser@example.com");

        mockMvc.perform(patch("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void changeUserRole_ShouldUpdateIsAdmin() throws Exception {
        boolean newIsAdmin = true;

        mockMvc.perform(patch("/users/" + existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newIsAdmin)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void deleteUser_ShouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/" + existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        boolean exists = userRepository.findById(existingUser.getId()).isPresent();
        assert !exists;
    }
}
