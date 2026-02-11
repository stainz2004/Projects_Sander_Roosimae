package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.signin.LoginRequestDto;
import com.example.veebilehekala.controller.signin.ProfileResponseDto;
import com.example.veebilehekala.controller.signin.SignInRequestDto;
import com.example.veebilehekala.controller.signin.SignInRequestResponseDto;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Role;
import com.example.veebilehekala.exception.ApplicationException;
import com.example.veebilehekala.mapper.CustomerMapper;
import com.example.veebilehekala.mapper.CustomerMapperImpl;
import com.example.veebilehekala.mapper.SignInRequestMapper;
import com.example.veebilehekala.mapper.SignInRequestMapperImpl;
import com.example.veebilehekala.repository.RoleRepository;
import com.example.veebilehekala.repository.SignInRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private  SignInRepository customerRepository;
    @Spy
    private  SignInRequestMapper signInRequestMapper = new SignInRequestMapperImpl();
    @Spy
    private  CustomerMapper customerMapper = new CustomerMapperImpl();
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  JwtTokenService jwtTokenService;
    @Mock
    private  RoleRepository roleRepository;

    @InjectMocks
    private SignupService signUpService;

    private SignInRequestResponseDto signInRequestResponseDto;
    private SignInRequestDto signInRequestDto;
    private Customer customer;
    private Role role;
    private ProfileResponseDto profileResponseDto;
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsImlhdCI6MTY4MjY1NzIyMiwiZXhwIjoxNjgyNjU3NTgyfQ.e9I62R2iXwPwNQoYsV4u2B-tDxXbU-tU8ZJHnQVHkM";
    private LoginRequestDto loginRequestDto;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
        customer = Customer.builder()
                .id(1L)
                .name("Test User")
                .username("testuser")
                .email("testuser@gmail.com")
                .password("password")
                .roles(Set.of(role))
                .build();
        signInRequestResponseDto = SignInRequestResponseDto
                .builder()
                .token(token)
                .build();
        signInRequestDto = SignInRequestDto
                .builder()
                .name("Test User")
                .username("test.user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        profileResponseDto = ProfileResponseDto
                .builder()
                .username("testuser")
                .roles(List.of("ROLE_USER"))
                .id(1)
                .build();
        loginRequestDto = LoginRequestDto
                .builder()
                .username("testuser")
                .password("password")
                .build();
    }

    @Test
    void addCustomerWorks() {
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.ofNullable(role));
        given(customerRepository.save(any(Customer.class))).willReturn(customer);
        given(jwtTokenService.generateToken(any(Customer.class))).willReturn(token);

        SignInRequestResponseDto responseDto = signUpService.addCustomer(signInRequestDto);

        assertEquals(signInRequestResponseDto, responseDto);
    }

    @Test
    void testLoginWorks() {
        given(customerRepository.findByUsernameIgnoreCase("testuser")).willReturn(Optional.of(customer));
        given(passwordEncoder.matches("password", customer.getPassword())).willReturn(true);
        given(jwtTokenService.generateToken(any(Customer.class))).willReturn(token);

        SignInRequestResponseDto responseDto = signUpService.checkLogin(loginRequestDto);

        assertEquals(signInRequestResponseDto, responseDto);
    }

    @Test
    void testLoginDoesNotWork() {
        given(customerRepository.findByUsernameIgnoreCase("testuser")).willReturn(Optional.of(customer));
        given(passwordEncoder.matches("password", customer.getPassword())).willReturn(false);

        assertThrows(ApplicationException.class, () -> {
            signUpService.checkLogin(loginRequestDto);
        });
    }

    @Test
    void getCurrentUserWorks() {
        given(customerRepository.findByUsernameIgnoreCase("testuser")).willReturn(Optional.of(customer));

        ProfileResponseDto responseDto = signUpService.getCurrentUser("testuser");

        assertEquals(profileResponseDto, responseDto);
    }
}
