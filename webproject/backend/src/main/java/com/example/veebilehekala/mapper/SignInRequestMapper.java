package com.example.veebilehekala.mapper;

import com.example.veebilehekala.controller.signin.ProfileResponseDto;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignInRequestMapper {
    @Mapping(target = "roles", source = "roles")
    ProfileResponseDto mapToDtoProfile(Customer customer);

    default List<String> map(Set<Role> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(Role::getName)
                .toList();
    }
}
