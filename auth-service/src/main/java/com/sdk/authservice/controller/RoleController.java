package com.sdk.authservice.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdk.authservice.response.ApiResponse;
import com.sdk.authservice.service.RoleService;

@RestController
@RequestMapping("/auth-service/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping(value = "", name = "GET ALL ROLES", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllRoles() {
        ApiResponse response = roleService.getAllRoles();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping(value = "/{id}", name = "GET ROLE BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getRole(@PathVariable("id") UUID id) {
        ApiResponse response = roleService.getRole(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
