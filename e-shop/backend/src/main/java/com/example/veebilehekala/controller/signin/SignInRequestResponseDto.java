package com.example.veebilehekala.controller.signin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SignInRequestResponseDto {
    @Schema(description = "Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsImlhdCI6MTY4MjY1NzIyMiwiZXhwIjoxNjgyNjU3NTgyfQ.e9I62R2iXwPwNQoYsV4u2B-tDxXbU-tU8ZJHnQVHkM")
    private String token;
}
