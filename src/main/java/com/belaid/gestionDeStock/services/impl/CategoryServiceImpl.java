package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.CategoryDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.model.Category;
import com.belaid.gestionDeStock.repository.CategoryRepository;
import com.belaid.gestionDeStock.services.CategoryService;
import com.belaid.gestionDeStock.validator.CategoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        List<String> errors = CategoryValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Category n'est pas valid {}", dto);
            throw new InvalidEntityException("Category n'est pas valid", ErrorCodes.CATEGORY_NOT_VALID, errors);
        }

        Category saveCategory = categoryRepository.save(CategoryDto.toEntity(dto));
        return CategoryDto.fromEntity(saveCategory);
    }

    @Override
    public CategoryDto findById(Integer id) {
        if (id == null) {
            log.error("Category ID est null");
            return null;
        }

        Optional<Category> category = categoryRepository.findById(id);

        CategoryDto dto = CategoryDto.fromEntity(category.get());

        return Optional.of(dto).orElseThrow(() -> new EntityNotFoundException("Aucun category avec ID " + id + " n'ete trouver dans BDD", ErrorCodes.CATEGORY_NOT_FOUND));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Category ID est null");
            return;
        }
        categoryRepository.findById(id);
    }
}