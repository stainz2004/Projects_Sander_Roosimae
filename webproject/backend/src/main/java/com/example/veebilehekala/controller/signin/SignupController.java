package com.example.veebilehekala.controller.signin;

import com.example.veebilehekala.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@Tag(name = "Signup", description = "Signup API")
public class SignupController {

    private final SignupService signupService;

    @PostMapping("public/signin")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Signup", description = "Signup new user")
    @ApiResponse(responseCode = "200", description = "User signed up")
    public ResponseEntity<Object> addCustomer(@Valid @RequestBody SignInRequestDto signInRequestDto) {
        SignInRequestResponseDto response = signupService.addCustomer(signInRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("public/login")
    @Operation(summary = "Login", description = "Login user")
    @ApiResponse(responseCode = "200", description = "User logged in")
    public ResponseEntity<Object> customerLogin(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        SignInRequestResponseDto response = signupService.checkLogin(loginRequestDto);
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("me")
    @Operation(summary = "Get current user", description = "Get current user")
    @ApiResponse(responseCode = "200", description = "Found and return current user")
    public ResponseEntity<Object> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        String username = authentication.getName();
        ProfileResponseDto profileDto = signupService.getCurrentUser(username);

        return ResponseEntity.ok().body(profileDto);
    }
}
