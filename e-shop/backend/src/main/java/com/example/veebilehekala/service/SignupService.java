package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.signin.LoginRequestDto;
import com.example.veebilehekala.controller.signin.ProfileResponseDto;
import com.example.veebilehekala.controller.signin.SignInRequestDto;
import com.example.veebilehekala.controller.signin.SignInRequestResponseDto;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Role;
import com.example.veebilehekala.exception.ApplicationException;
import com.example.veebilehekala.mapper.CustomerMapper;
import com.example.veebilehekala.mapper.SignInRequestMapper;
import com.example.veebilehekala.repository.RoleRepository;
import com.example.veebilehekala.repository.SignInRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupService {

    private final SignInRepository customerRepository;
    private final SignInRequestMapper signInRequestMapper;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final RoleRepository roleRepository;

    @Transactional
    public SignInRequestResponseDto addCustomer(SignInRequestDto signInRequestDto) {
        if (customerRepository.findByUsernameIgnoreCase(signInRequestDto.getUsername()).isPresent()) {
            throw new ApplicationException("Username already exists", 409);
        }
        if (customerRepository.findByEmailIgnoreCase(signInRequestDto.getEmail()).isPresent()) {
            throw new ApplicationException("Email already exists", 409);
        }
        Customer customer = customerMapper.mapToCustomer(signInRequestDto);
        customer.setPassword(passwordEncoder.encode(signInRequestDto.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));
        customer.getRoles().add(userRole);
        customerRepository.save(customer);
        log.info("Customer created: {}", customer.getId());
        String token = jwtTokenService.generateToken(customer);

        return new SignInRequestResponseDto(token);
    }

    public SignInRequestResponseDto checkLogin(LoginRequestDto loginRequestDto) {
        Customer customer = customerRepository.findByUsernameIgnoreCase(loginRequestDto.getUsername()).orElseThrow( () -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), customer.getPassword())) {
            throw new ApplicationException("Invalid password", 401);
        }

        String token = jwtTokenService.generateToken(customer);
        return new SignInRequestResponseDto(token);
    }

    public ProfileResponseDto getCurrentUser(String username) {
        Customer customer = customerRepository.findByUsernameIgnoreCase(username).orElseThrow( () -> new ApplicationException("User not found", 404));

        return signInRequestMapper.mapToDtoProfile(customer);
    }

}
