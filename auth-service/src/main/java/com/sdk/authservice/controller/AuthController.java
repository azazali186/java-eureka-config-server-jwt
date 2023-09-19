package com.sdk.authservice.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdk.authservice.request.LoginRequest;
import com.sdk.authservice.request.RegisterRequest;
import com.sdk.authservice.response.ApiResponse;
import com.sdk.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/auth-service/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthService authService;

    @PostMapping(value = "/admin/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> adminRegisterHandler(@RequestBody RegisterRequest req) {
        ApiResponse response = authService.registerHandler(req, "ADMIN");
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse> adminLoginHandler(@RequestBody LoginRequest req) {
        ApiResponse response = authService.loginHandler(req, "ADMIN");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> registerHandler(@RequestBody RegisterRequest req) {
        ApiResponse response = authService.registerHandler(req, "MEMBERS");
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/driver/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> driverDegisterHandler(@RequestBody RegisterRequest req) {
        ApiResponse response = authService.registerHandler(req, "DRIVER");
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginHandler(@RequestBody LoginRequest req) {
        ApiResponse response = authService.loginHandler(req, "MEMBERS");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/driver/login")
    public ResponseEntity<ApiResponse> driverLoginHandler(@RequestBody LoginRequest req) {
        ApiResponse response = authService.loginHandler(req, "DRIVER");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @GetMapping(value = "/user/{id}", name="GET USER BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") UUID id) {
        ApiResponse response = authService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
