package com.bugsolver.service;

import com.bugsolver.entity.Category;
import com.bugsolver.exception.category.AlreadyExistsCategoryException;
import com.bugsolver.exception.category.CategoryNotFoundException;
import com.bugsolver.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(Category category){
        if(categoryRepository.existsByName(category.getName())){
            throw new AlreadyExistsCategoryException();
        }

        return categoryRepository.save(category);
    }

    public Category update(Long id, Category category){
        if(!categoryRepository.existsById(id)){
            throw new CategoryNotFoundException();
        }
        else if(categoryRepository.existsByName(category.getName())){
            throw new AlreadyExistsCategoryException();
        }

        category.setId(id);
        return categoryRepository.save(category);
    }

    public void deleteById(Long id){
        if(!categoryRepository.existsById(id)){
            throw new CategoryNotFoundException();
        }

        categoryRepository.deleteById(id);
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException()
        );
    }

    public Category findByName(String name){
        return categoryRepository.findByName(name).orElseThrow(
                () -> new CategoryNotFoundException()
        );
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
