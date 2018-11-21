package com.example.jpa.controller;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Category;
import com.example.jpa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @PostMapping("/posts")
    public Category createCategory(@Valid @RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/categories/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, @Valid @RequestBody Category categoryRequest) {
        return categoryRepository.findById(categoryId).map(category -> {
            category.setTitle(categoryRequest.getTitle());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + categoryId + " not found"));
    }


    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        return categoryRepository.findById(categoryId).map(category -> {
            categoryRepository.delete(category);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + categoryId + " not found"));
    }

}