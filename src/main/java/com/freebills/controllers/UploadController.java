package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.UploadImageRequestDTO;
import com.freebills.controllers.dtos.responses.UploadResponseDTO;
import com.freebills.controllers.mappers.FileReferenceMapper;
import com.freebills.domain.FileReference;
import com.freebills.gateways.StorageGateway;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Upload Controller")
@RequestMapping("v1/")
public class UploadController {

    private final StorageGateway storageGateway;
    private final FileReferenceMapper fileReferenceMapper;

    @PostMapping("uploads/images")
    public UploadResponseDTO uploadRequest(@RequestBody @Valid UploadImageRequestDTO request) {
        final FileReference domain = fileReferenceMapper.toDomain(request);
        return fileReferenceMapper.fromDomain(storageGateway.generateUploadUrl(domain));
    }
}
