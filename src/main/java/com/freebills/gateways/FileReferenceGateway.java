package com.freebills.gateways;

import com.freebills.domain.FileReference;
import com.freebills.gateways.mapper.FileReferenceGatewayMapper;
import com.freebills.repositories.FileReferenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileReferenceGateway {

    private final FileReferenceRepository fileReferenceRepository;
    private final FileReferenceGatewayMapper fileReferenceMapper;

    @Transactional
    public FileReference save(final FileReference fileReference) {
        return fileReferenceMapper.toDomain(fileReferenceRepository.save(fileReferenceMapper.toEntity(fileReference)));
    }
}
