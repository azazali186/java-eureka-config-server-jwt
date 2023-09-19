package com.sdk.authservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sdk.authservice.entity.DocumentEntity;

public interface DocumentRepo extends JpaRepository<DocumentEntity, UUID> {

}