package com.freebills.controllers.dtos.requests;

public record UploadImageRequestDTO(
         String fileName,
         String contentType,
         Long contentLength
) { }
