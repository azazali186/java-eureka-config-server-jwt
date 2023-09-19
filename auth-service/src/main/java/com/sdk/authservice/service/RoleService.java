package com.sdk.authservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sdk.authservice.entity.RoleEntity;
import com.sdk.authservice.repository.RoleRepo;
import com.sdk.authservice.response.ApiResponse;

@Service
public class RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private RoleRepo roleRepo;

    public ApiResponse getAllRoles() {
        List<RoleEntity> roles = roleRepo.findAll();
        ApiResponse<RoleEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Successfully retrieved all",
                roles);
        return response;
    }

    public ApiResponse getRole(UUID id) {
        Optional<RoleEntity> optionalRole = roleRepo.findById(id);
        ApiResponse<RoleEntity> response = new ApiResponse<>();
        if (!optionalRole.isPresent()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("INVALID_ROLE_ID");
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Successfully");
            response.setData(optionalRole.get());
        }
        return response;
    }

}
