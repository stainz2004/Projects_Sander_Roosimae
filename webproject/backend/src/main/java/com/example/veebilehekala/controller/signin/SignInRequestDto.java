package com.example.veebilehekala.controller.signin;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class SignInRequestDto {
    @NotBlank(message = "Name is required")
    @Schema(description = "Name", example = "John Doe")
    private String name;
    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "john.doe")
    private String username;
    @NotBlank(message = "Email is required")
    @Schema(description = "Email", example = "john.doe@example.com")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 1, message = "Password must be at least 8 characters long")
    @Schema(description = "Password", example = "password")
    private String password;
}
