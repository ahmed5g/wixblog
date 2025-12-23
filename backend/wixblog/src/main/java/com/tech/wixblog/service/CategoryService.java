package com.tech.wixblog.service;

import com.tech.wixblog.exception.ResourceNotFoundException;
import com.tech.wixblog.model.Category;
import com.tech.wixblog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public Category createCategory(String name, boolean isFeatured, Long parentId) {
        Category parent = null;
        if (parentId != null) {
            parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
        }

        Category category = Category.builder()
                .name(name)
                .slug(generateSlug(name))
                .parent(parent)
                .featured(isFeatured)
                .build();

        if (parent != null) {
            parent.getChildren().add(category);
        }

        return categoryRepository.save(category);
    }
    
    @Transactional(readOnly = true)
    public List<Category> getAllRootCategories() {
        return categoryRepository.findByParentIsNullOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Category> getCategoryTree() {
        return categoryRepository.findRootCategoriesWithChildren();
    }
    
    @Transactional(readOnly = true)
    public Category getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + slug));
    }


    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }



    // Inside CategoryService.java
    private String generateSlug(String name) {
        if (name == null) return "";

        // 1. Standardize formatting
        String baseSlug = name.toLowerCase() // CRUCIAL: Lowercase here
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();

        // 2. Handle uniqueness
        String slug = baseSlug;
        int counter = 1;
        while (categoryRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }
}