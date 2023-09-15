package com.sdk.authservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sdk.authservice.entity.RoleEntity;
import com.sdk.authservice.entity.UserEntity;
import com.sdk.authservice.entity.UserWithToken;
import com.sdk.authservice.exception.InvalidCredentialsException;
import com.sdk.authservice.exception.MissingFieldException;
import com.sdk.authservice.exception.UserAlreadyExistsException;
import com.sdk.authservice.exception.UserNotFoundException;
import com.sdk.authservice.repository.AuthRepo;
import com.sdk.authservice.repository.RoleRepo;
import com.sdk.authservice.request.LoginRequest;
import com.sdk.authservice.request.RegisterRequest;
import com.sdk.authservice.response.ApiResponse;
import com.sdk.authservice.utils.JwtUtil;

@Service
public class AuthService  implements UserDetailsService {

    // @Autowired User users;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity appUser = authRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(),
                new ArrayList<>());
    }

    public ApiResponse registerHandler(RegisterRequest req) throws UserAlreadyExistsException{
        if (req.getEmail() == null || req.getUsername() == null || req.getPassword() == null) {
            throw new MissingFieldException("Email, Username, and Password are required fields.");
        }

        Optional<UserEntity> existingUser = authRepo.findByEmail(req.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with the same email already exists.");
        }

        Optional<RoleEntity> existRole = roleRepo.findByName("MEMBER");

        RoleEntity role = new RoleEntity();

        if (existRole.isPresent()) {
            role = existRole.get();
        }else {
            role.setName("MEMBER");
            role.setDesc("MEMBER ROLE");
            roleRepo.save(role);
            role = roleRepo.findByName("MEMBER").get();
            
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(req.getUsername());
        String hashedPassword = hashPassword(req.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setEmail(req.getEmail());
        newUser.setCreatedAt(new Date());
        newUser.setRoleId(role);
        authRepo.save(newUser);

        LOGGER.info("User created: {}", newUser);

        ApiResponse<UserEntity> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User registered successfully",
                newUser);

        return response;
    }

    private String hashPassword(String plainTextPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(plainTextPassword);
    }

    public ApiResponse loginHandler(LoginRequest req) throws UserNotFoundException, InvalidCredentialsException {
        Optional<UserEntity> optionalUserEntity = authRepo.findByEmail(req.getEmail());
    
        // If user not found, throw exception
        if (!optionalUserEntity.isPresent()) {
            throw new UserNotFoundException("User not found with email: " + req.getEmail());
        }
    
        UserEntity userEntity = optionalUserEntity.get();
    
        // Assuming you have a utility method to check the password
        if (!isPasswordValid(req.getPassword(), userEntity.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
    
        // For security reasons, you may want to remove or nullify sensitive data
        // like the password hash before sending the user object in the response
        userEntity.setPassword(null);

        String jwtToken = JwtUtil.getInstance().generateToken(userEntity.getEmail());

        UserWithToken userWithToken = new UserWithToken(userEntity, jwtToken);

        ApiResponse<UserWithToken> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            "Login successful",
            userWithToken
        );
    
        return response;
    }
    

    private boolean isPasswordValid(String plainTextPassword, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(plainTextPassword, hashedPassword);
    }

}
