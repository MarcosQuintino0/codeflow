package com.bugsolver.service;

import com.bugsolver.entity.Category;
import com.bugsolver.exception.category.AlreadyExistsCategoryException;
import com.bugsolver.exception.category.CategoryNotFoundException;
import com.bugsolver.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryServiceTests {

    @Autowired
    private CategoryService categoryService;
    @MockBean
    private CategoryRepository categoryRepository;

    @DisplayName("WHEN save a non existent category SHOULD save and return new category")
    @Test
    public void saveNewCategory(){
        Category category = new Category();
        category.setId(1L);
        category.setName("Java");

        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        assertThat(categoryService.save(category)).isEqualTo(category);
    }

    @DisplayName("WHEN save an existent category SHOULD throw AlreadyExistsCategoryException")
    @Test
    public void saveAlreadyExistingCategory(){
        Category category = new Category();
        category.setId(1L);
        category.setName("Java");

        when(categoryRepository.existsByName(anyString())).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Assertions.assertThrows(AlreadyExistsCategoryException.class,
                () -> categoryService.save(category)
        );
    }

    @DisplayName("WHEN update a category with new name and valid id SHOULD update and return category")
    @Test
    public void updateCategoryWithNonExistingNameAndValidId(){
        Category category = new Category();
        category.setId(1L);

        Category updatedCategory = new Category();
        updatedCategory.setName("Kotlin");

        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        assertThat(categoryService.update(category.getId(),updatedCategory)).isEqualTo(updatedCategory);
    }

    @DisplayName("WHEN update a category with existing name and valid id SHOULD throw AlreadyExistCategoryException")
    @Test
    public void updateCategoryWithExistingNameAndValidId(){
        Category category = new Category();
        category.setId(1L);

        Category updatedCategory = new Category();
        updatedCategory.setName("Java");

        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.existsByName(anyString())).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Assertions.assertThrows(AlreadyExistsCategoryException.class,
                () -> categoryService.update(category.getId(), updatedCategory)
        );
    }

    @DisplayName("WHEN update a category with new name and invalid id SHOULD throw CategoryNotFoundException")
    @Test
    public void updateCategoryWithNewNameAndInvalidId(){

        Category updatedCategory = new Category();
        updatedCategory.setName("Java");

        when(categoryRepository.existsById(anyLong())).thenReturn(false);
        when(categoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.update(anyLong(), updatedCategory)
        );
    }

    @DisplayName("WHEN delete category with valid id SHOULD delete category")
    @Test
    public void deleteCategoryWithValidId(){

        Category category = new Category();
        category.setId(1L);
        category.setName("Java");

        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(anyLong());

        categoryService.deleteById(category.getId());
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @DisplayName("WHEN delete category with invalid id SHOULD throw CategoryNotFoundException")
    @Test
    public void deleteCategoryWithInvalidId(){

        when(categoryRepository.existsById(anyLong())).thenReturn(false);
        doNothing().when(categoryRepository).deleteById(anyLong());


        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteById(anyLong())
        );
    }

    @DisplayName("WHEN find category with valid id SHOULD return category")
    @Test
    public void findCategoryWithValidId(){

        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        assertThat(categoryService.findById(category.getId())).isEqualTo(category);
    }

    @DisplayName("WHEN find category with invalid id SHOULD throw CategoryNotFoundException")
    @Test
    public void findCategoryWithInvalidId(){

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.findById(anyLong())
        );
    }

    @DisplayName("WHEN find category with valid name SHOULD return category")
    @Test
    public void findCategoryWithValidName(){

        Category category = new Category();
        category.setId(1L);
        category.setName("Java");

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        assertThat(categoryService.findByName(category.getName())).isEqualTo(category);
    }

    @DisplayName("WHEN find category with invalid name SHOULD throw CategoryNotFoundException")
    @Test
    public void findCategoryWithInvalidName(){

        Category category = new Category();
        category.setId(1L);
        category.setName("Java");

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(CategoryNotFoundException.class,
                () -> categoryService.findByName(anyString())
        );
    }

    @DisplayName("WHEN find all categories SHOULD return all categories")
    @Test
    public void findAllCategories(){

        Category category = new Category();
        category.setId(1L);
        category.setName("Java");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Kotlin");

        when(categoryRepository.findAll()).thenReturn(List.of(category,category2));
        assertThat(categoryService.findAll()).isEqualTo(List.of(category, category2));
    }
}
