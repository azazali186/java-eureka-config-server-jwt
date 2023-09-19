package com.sdk.authservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sdk.authservice.entity.PermissionEntity;
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
public class AuthService implements UserDetailsService {

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

        appUser.getRoleId().getPermissions().size(); // Ensure permissions are loaded
        List<PermissionEntity> permissions = appUser.getRoleId().getPermissions();

        List<GrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(),
                authorities);
    }

    public ApiResponse registerHandler(RegisterRequest req, String roleName) throws UserAlreadyExistsException {
        if (req.getEmail() == null || req.getUsername() == null || req.getPassword() == null) {
            throw new MissingFieldException("Email, Username, and Password are required fields.");
        }

        Optional<UserEntity> existingUser = authRepo.findByEmailAndRoleIdName(req.getEmail(), roleName);

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with the same email already exists.");
        }

        Optional<RoleEntity> existRole = roleRepo.findByName(roleName);

        RoleEntity role = new RoleEntity();

        if (existRole.isPresent()) {
            role = existRole.get();
        } else {
            role.setName(roleName);
            role.setDesc(roleName + " ROLE");
            roleRepo.save(role);
            role = roleRepo.findByName(roleName).get();

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

        newUser.setPassword(null);

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

    public ApiResponse loginHandler(LoginRequest req, String roleName)
            throws UserNotFoundException, InvalidCredentialsException {
        Optional<UserEntity> optionalUserEntity = authRepo.findByEmailAndRoleIdName(req.getEmail(), roleName);

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

        String jwtToken = JwtUtil.getInstance().generateToken(userEntity.getEmail(), userEntity.getId());

        UserWithToken userWithToken = new UserWithToken(userEntity, jwtToken);

        ApiResponse<UserWithToken> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Login successful",
                userWithToken);

        return response;
    }

    private boolean isPasswordValid(String plainTextPassword, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(plainTextPassword, hashedPassword);
    }

    public UserEntity findById(UUID id) throws UserNotFoundException {
        Optional<UserEntity> optionalUserEntity = authRepo.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        return optionalUserEntity.get();
    }

    public ApiResponse getUserById(UUID id) {

        UserEntity user = findById(id);

        ApiResponse<UserEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Successfully",
                user);

        return response;
    }

}
