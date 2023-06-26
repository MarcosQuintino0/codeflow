package com.bugsolver.controller;

import com.bugsolver.entity.Category;
import com.bugsolver.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id){
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable("name") String name){
        return ResponseEntity.ok(categoryService.findByName(name));
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping("")
    public ResponseEntity<Category> createNewCategory(@Valid @RequestBody Category category){
        Category categoryCreated = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryCreated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("id") Long id){
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategoryById(@PathVariable("id") Long id, @Valid @RequestBody Category newCategory){
        return ResponseEntity.ok(categoryService.update(id, newCategory));
    }
}
