package com.example.veebilehekala.controller.signin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class LoginRequestDto {
    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "john.doe")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 1, message = "Password must be at least 8 characters long")
    @Schema(description = "Password", example = "password")
    private String password;
}
