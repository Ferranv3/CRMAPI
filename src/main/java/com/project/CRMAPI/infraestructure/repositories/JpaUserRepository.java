package com.project.CRMAPI.infraestructure.repositories;

import com.project.CRMAPI.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String username);

    Optional<User> findByUsername(String username);
}
