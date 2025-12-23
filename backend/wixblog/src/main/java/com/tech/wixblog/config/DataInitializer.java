/*
package com.tech.wixblog.config;

import com.tech.wixblog.model.Category;
import com.tech.wixblog.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final CategoryRepository categoryRepository;

    @PostConstruct
    @Transactional
    public void initDefaultCategories () {
        if (categoryRepository.count() == 0) {
            // Create root categories
            Category technology = createCategory("Technology", "tech");
            Category lifestyle = createCategory("Lifestyle", "lifestyle");
            Category business = createCategory("Business", "business");
            // Create subcategories for Lifestyle
            Category family = createCategory("Family", "family", lifestyle);
            Category health = createCategory("Health & Wellness", "health", lifestyle);
            Category travel = createCategory("Travel", "travel", lifestyle);
            // Create subcategories for Family
            createCategory("Parenting", "parenting", family);
            createCategory("Relationships", "relationships", family);
            createCategory("Home & Garden", "home-garden", family);
        }
    }

    private Category createCategory (String name, String slug) {
        return createCategory(name, slug, null);
    }

    private Category createCategory (String name, String slug, Category parent) {
        Category category = Category.builder()
                .name(name)
                .slug(slug)
                .parent(parent)
                .featured(true)
                .build();
        return categoryRepository.save(category);
    }
}*/
