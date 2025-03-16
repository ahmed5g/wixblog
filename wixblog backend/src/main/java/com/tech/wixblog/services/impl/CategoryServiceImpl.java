package com.tech.wixblog.services.impl;

import com.tech.wixblog.domain.entities.Category;
import com.tech.wixblog.repositories.CategoryRepository;
import com.tech.wixblog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories () {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category already exists with the name: " + category.getName());
        }
        return categoryRepository.save(category);
    }

}
