package com.project.CRMAPI.application.services;

import com.project.CRMAPI.application.ports.UserRepository;
import com.project.CRMAPI.domain.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> getUserById(UUID userId) {
        this.verifyActualUserisAdmin();
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        this.verifyActualUserisAdmin();
        return userRepository.findAll();
    }

    public User createUser(User user) {
        this.verifyActualUserisAdmin();
        return userRepository.save(user);
    }

    public void deleteUser(UUID userId) {
        this.verifyActualUserisAdmin();
        userRepository.deleteById(userId);
    }

    private void verifyActualUserisAdmin() {
        if(!this.getActualUser().isAdmin()) throw new IllegalCallerException("This user doesn't have enough rights");
    }

    private User getActualUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
