package com.saorim.flashcard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saorim.flashcard.model.Category;
import com.saorim.flashcard.model.User;
import com.saorim.flashcard.repository.CategoryRepository;
import com.saorim.flashcard.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Category createCategory(Category category, String username) {
        User user = getUserByUsername(username);
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(String username) {
        User user = getUserByUsername(username);
        return categoryRepository.findByUserId(user.getId());
    }

    public Category getCategory(Long id, String username) {
        User user = getUserByUsername(username);
        return categoryRepository.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    public Category updateCategory(Long id, Category categoryDetails, String username) {
        Category category = getCategory(id, username);
        
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id, String username) {
        Category category = getCategory(id, username);
        categoryRepository.delete(category);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
