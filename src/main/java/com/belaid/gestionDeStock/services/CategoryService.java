package com.belaid.gestionDeStock.services;

import com.belaid.gestionDeStock.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryDto dto);

    CategoryDto findById(Integer id);

    List<CategoryDto> findAll();

    CategoryDto findByCode(String code);

    void delete(Integer id);
}
