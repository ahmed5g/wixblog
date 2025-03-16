package com.tech.wixblog.services;

import com.tech.wixblog.domain.entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> listCategories();
    Category createCategory(Category category);
}
