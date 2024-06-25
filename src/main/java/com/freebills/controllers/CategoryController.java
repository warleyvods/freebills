package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.CategoryPostRequestDTO;
import com.freebills.controllers.dtos.requests.CategoryPutRequestDTO;
import com.freebills.controllers.dtos.responses.CategoryResponseDTO;
import com.freebills.controllers.mappers.CategoryMapper;
import com.freebills.domain.Category;
import com.freebills.usecases.CreateCategory;
import com.freebills.usecases.DeleteCategory;
import com.freebills.usecases.FindCategory;
import com.freebills.usecases.UpdateCategory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Tag(name = "Category Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/categories")
public class CategoryController {

    private final CategoryMapper mapper;
    private final FindCategory findCategory;
    private final UpdateCategory updateCategory;
    private final CreateCategory createCategory;
    private final DeleteCategory deleteCategory;

    @ResponseStatus(CREATED)
    @PostMapping
    public CategoryResponseDTO save(@RequestBody @Valid final CategoryPostRequestDTO request, Principal principal) {
        return mapper.toDTO(createCategory.execute(mapper.toDomain(request, principal.getName())));
    }

    @ResponseStatus(OK)
    @GetMapping
    public Page<CategoryResponseDTO> findAll(@RequestParam(required = false) final String keyword,
                                             @RequestParam(required = false) final String categoryType,
                                             @RequestParam(required = false) final Boolean archived,
                                             final Principal principal,
                                             final Pageable pageable) {
        return findCategory.findAll(principal.getName(), keyword, categoryType, archived, pageable).map(mapper::toDTO);
    }

    @ResponseStatus(OK)
    @PatchMapping("{id}")
    public CategoryResponseDTO toggleArchiveCategory(@PathVariable final Long id, Principal principal) {
        final Category category = findCategory.findById(id, principal.getName());
        return mapper.toDTO(updateCategory.execute(mapper.toggleArchived(category)));
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    public CategoryResponseDTO findById(@PathVariable final Long id, Principal principal) {
        return mapper.toDTO(findCategory.findById(id, principal.getName()));
    }

    @ResponseStatus(OK)
    @PutMapping
    public CategoryResponseDTO update(@RequestBody @Valid final CategoryPutRequestDTO category, final Principal principal) {
        final Category foundCategory = findCategory.findById(category.id(), principal.getName());
        return mapper.toDTO(updateCategory.execute(mapper.update(category, foundCategory)));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{categoryId}")
    public void deleteCategory(@PathVariable final Long categoryId, final Principal principal) {
        deleteCategory.execute(categoryId, principal.getName());
    }
}
