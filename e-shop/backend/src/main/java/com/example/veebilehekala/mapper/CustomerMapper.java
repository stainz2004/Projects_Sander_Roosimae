package com.example.veebilehekala.mapper;

import com.example.veebilehekala.controller.signin.SignInRequestDto;
import com.example.veebilehekala.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    Customer mapToCustomer(SignInRequestDto signInRequestDto);
}
