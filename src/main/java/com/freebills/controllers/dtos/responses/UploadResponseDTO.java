package com.freebills.controllers.dtos.responses;

import java.util.UUID;

public record UploadResponseDTO(
        UUID fileReferenceId,
        String uploadSignedUrl
) { }