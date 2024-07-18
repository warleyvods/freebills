package com.freebills.controllers.mappers;

import com.freebills.controllers.dtos.requests.CategoryPostRequestDTO;
import com.freebills.controllers.dtos.requests.CategoryPutRequestDTO;
import com.freebills.controllers.dtos.responses.CategoryResponseDTO;
import com.freebills.domain.Category;
import com.freebills.usecases.FindTransaction;
import com.freebills.usecases.FindUser;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", uses = { FindTransaction.class, FindUser.class }, unmappedTargetPolicy = IGNORE)
public interface CategoryMapper {

    @Mapping(source = "username", target = "user")
    @Mapping(target = "archived", constant = "false")
    Category toDomain(CategoryPostRequestDTO category, String username);

    CategoryResponseDTO toDTO(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category update(CategoryPutRequestDTO categoryPutRequestDTO, @MappingTarget Category category);

    default Category toggleArchived(@MappingTarget Category category) {
        category.setArchived(!category.getArchived());
        return category;
    }
}
