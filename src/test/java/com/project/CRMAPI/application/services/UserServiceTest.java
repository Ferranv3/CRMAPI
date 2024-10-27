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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

        User user = new User(UUID.randomUUID(), "admin",
                "passTest","email@email.com", true);
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

        User user = new User(UUID.randomUUID(), "newuser",
                "passTest", "email@email.com", true);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        User createdUser = userService.createUser(user);

        assertEquals("newuser", createdUser.getUsername());
    }

    @Test
    void testUpdateUser() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        User user = new User(UUID.randomUUID(), "newuser",
                "passTest", "email@email.com", true);
        User userModified = new User(user.getId(), "differentUser",
                "passTest", "different@email.com", user.isAdmin());
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userModified);
        User updatedUser = userService.updateUser(user);

        assertEquals(user.getId(), updatedUser.getId());
        assertNotEquals(user.getUsername(), updatedUser.getUsername());
        assertNotEquals(user.getEmail(), updatedUser.getEmail());
    }

    @Test
    void testChangeUserRole() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        User userLogged = new User(UUID.randomUUID(), "newuser",
                "passTest", "email@email.com", true);
        User userToModifiy = new User(UUID.randomUUID(), "differentUser",
                "passTest", "different@email.com", false);
        User userModified = new User(userToModifiy.getId(), userToModifiy.getUsername(),
                userToModifiy.getPassword(), userToModifiy.getEmail(), true);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(userLogged));
        when(userRepository.findById(any())).thenReturn(Optional.of(userToModifiy));
        when(userRepository.save(any(User.class))).thenReturn(userModified);
        User changedUser = userService.changeRole(userModified.getId(), true);

        assertEquals(userToModifiy.getId(), changedUser.getId());
        assertEquals(userToModifiy.getUsername(), changedUser.getUsername());
        assertNotEquals(userToModifiy.isAdmin(), changedUser.isAdmin());
    }
}
