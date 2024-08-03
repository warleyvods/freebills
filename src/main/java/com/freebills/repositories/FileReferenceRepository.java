package com.freebills.repositories;

import com.freebills.gateways.entities.FileReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface FileReferenceRepository extends JpaRepository<FileReferenceEntity, UUID> {

    List<FileReferenceEntity> findAllByTempIsTrueAndCreatedAtBefore(LocalDateTime createdAt);
}
