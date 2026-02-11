package com.example.veebilehekala.controller.signin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileResponseDto {
    @Schema(description = "Username", example = "john.doe")
    private String username;
    @Schema(description = "User ID", example = "1")
    private Integer id;
    @Schema(description = "Roles assigned to the user", example = "[\"USER\", \"ADMIN\"]")
    private List<String> roles;
}
