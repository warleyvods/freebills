package com.freebills.gateways.mapper;

import com.freebills.domain.FileReference;
import com.freebills.gateways.entities.FileReferenceEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface FileReferenceGatewayMapper {

    FileReference toDomain(FileReferenceEntity fileReferenceEntity);

    FileReferenceEntity toEntity(FileReference fileReference);

}
