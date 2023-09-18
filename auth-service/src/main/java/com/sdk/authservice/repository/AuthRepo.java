package com.sdk.authservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sdk.authservice.entity.UserEntity;

import java.util.Optional;

public interface AuthRepo  extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndRoleIdName(String email, String roleName);
    

}
