package com.sdk.authservice.repository;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdk.authservice.entity.PermissionEntity;

public interface PermissionRepo extends JpaRepository<PermissionEntity, UUID> {

    PermissionEntity findByName(String name);

}