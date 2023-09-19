package com.sdk.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdk.authservice.entity.RoleEntity;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepo  extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
    Optional<RoleEntity> findById(UUID id);
}
