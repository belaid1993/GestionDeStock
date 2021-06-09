package com.belaid.gestionDeStock.services.impl;

import com.belaid.gestionDeStock.dto.CategoryDto;
import com.belaid.gestionDeStock.exception.EntityNotFoundException;
import com.belaid.gestionDeStock.exception.ErrorCodes;
import com.belaid.gestionDeStock.exception.InvalidEntityException;
import com.belaid.gestionDeStock.services.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService service;

    @Test
    public void shouldSaveCategoryService() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(2)
                .code("Cat Test")
                .designation("description test")
                .idEntreprise(1)
                .build();

        CategoryDto saveCat = service.save(categoryDto);


        assertNotNull(categoryDto);
        assertNotNull(categoryDto.getId());

        assertEquals(categoryDto.getCode(), saveCat.getCode());
        assertEquals(categoryDto.getId(), saveCat.getId());
        assertEquals(categoryDto.getDesignation(), saveCat.getDesignation());

    }

    @Test
    public void shouldUpdateCategoryService() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(2)
                .code("Cat Test")
                .designation("description test")
                .idEntreprise(1)
                .build();

        CategoryDto saveCat = service.save(categoryDto);

        CategoryDto categoryToUpdate = saveCat;
        categoryToUpdate.setCode("Cat Update");

        saveCat = service.save(categoryToUpdate);


        assertNotNull(categoryToUpdate);
        assertNotNull(categoryToUpdate.getId());

        assertEquals(categoryToUpdate.getCode(), saveCat.getCode());
        assertEquals(categoryToUpdate.getId(), saveCat.getId());
        assertEquals(categoryToUpdate.getDesignation(), saveCat.getDesignation());

    }

    @Test
    public void shouldThrowInvalidEntityException() {
        CategoryDto categoryDto = CategoryDto.builder().build();

        InvalidEntityException expectedException = assertThrows(InvalidEntityException.class, () -> service.save(categoryDto));

        assertEquals(ErrorCodes.CATEGORY_NOT_VALID, expectedException.getErrorCodes());
        assertEquals(1, expectedException.getErrors().size());
        assertEquals("veuillez renseigner le code de la category", expectedException.getErrors().get(0));

    }

    @Test
    public void shouldEntityNotFoundException() {

        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> service.findById(0));

        assertEquals(ErrorCodes.CATEGORY_NOT_FOUND, expectedException.getErrorCodes());
        assertEquals("Aucune category avec l'ID = " + 0 + " n' ete trouve dans la BDD", expectedException.getMessage());

    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldEntityNotFoundException2() {
        service.findById(0);
    }


}