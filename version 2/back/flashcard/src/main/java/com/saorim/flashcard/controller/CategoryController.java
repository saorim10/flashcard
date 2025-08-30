package com.saorim.flashcard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saorim.flashcard.model.Category;
import com.saorim.flashcard.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category, Authentication authentication) {
        return ResponseEntity.ok(categoryService.createCategory(category, authentication.getName()));
    }

    @GetMapping
    @Operation(summary = "Get all categories for the current user")
    public ResponseEntity<List<Category>> getAllCategories(Authentication authentication) {
        return ResponseEntity.ok(categoryService.getAllCategories(authentication.getName()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific category by ID")
    public ResponseEntity<Category> getCategory(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(categoryService.getCategory(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, 
                                                 @RequestBody Category category,
                                                 Authentication authentication) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, Authentication authentication) {
        categoryService.deleteCategory(id, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
