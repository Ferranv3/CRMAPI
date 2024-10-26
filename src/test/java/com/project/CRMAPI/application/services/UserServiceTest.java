package com.project.CRMAPI.application.services;

import com.project.CRMAPI.application.ports.UserRepository;
import com.project.CRMAPI.domain.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @BeforeAll
    static void setup() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("userTest");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllUsers() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        User user = new User(UUID.randomUUID(), "admin", "passTest","email@email.com", true);
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("admin", users.get(0).getUsername());
    }

    @Test
    void testCreateUser() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        User user = new User(UUID.randomUUID(), "newuser", "passTest", "email@email.com", true);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        User createdUser = userService.createUser(user);

        assertEquals("newuser", createdUser.getUsername());
    }
}
