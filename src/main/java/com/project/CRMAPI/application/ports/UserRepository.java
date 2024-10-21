package com.project.CRMAPI.application.ports;

import com.project.CRMAPI.domain.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String email);
    User save(User user);
    void deleteById(UUID id);
}
