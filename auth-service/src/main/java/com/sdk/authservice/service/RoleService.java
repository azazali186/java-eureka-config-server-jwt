package com.sdk.authservice.service;

import java.util.List;

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
                HttpStatus.CREATED.value(),
                "Successfully retrieved all",
                roles);
        return response;
    }
    
}
