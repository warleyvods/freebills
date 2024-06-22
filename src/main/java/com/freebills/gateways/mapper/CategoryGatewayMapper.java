package com.freebills.gateways.mapper;

import com.freebills.domain.Category;
import com.freebills.gateways.entities.CategoryEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface CategoryGatewayMapper {

    Category toDomain(CategoryEntity categoryEntity);

    CategoryEntity toEntity(Category category);

}
