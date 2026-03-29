package com.example.veebilehekala.repository;

import com.example.veebilehekala.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignInRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUsernameIgnoreCase(String username);

    Optional<Object> findByEmailIgnoreCase(@NotBlank(message = "Email is required") @Email(message = "Invalid email address") String email);
}
