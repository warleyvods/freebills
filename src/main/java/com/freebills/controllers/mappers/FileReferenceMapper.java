package com.freebills.controllers.mappers;

import com.freebills.controllers.dtos.requests.UploadImageRequestDTO;
import com.freebills.controllers.dtos.responses.UploadResponseDTO;
import com.freebills.domain.FileReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileReferenceMapper {

    @Mapping(target = "temp", constant = "true")
    @Mapping(target = "type", constant = "IMAGE")
    FileReference toDomain(UploadImageRequestDTO request);

    @Mapping(source = "id", target = "fileReferenceId")
    @Mapping(source = "url", target = "uploadSignedUrl")
    UploadResponseDTO fromDomain(FileReference fileReference);

}
