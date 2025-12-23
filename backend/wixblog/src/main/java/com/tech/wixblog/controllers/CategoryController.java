package com.tech.wixblog.controllers;

import com.tech.wixblog.model.Category;
import com.tech.wixblog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/tree")
    public ResponseEntity<List<Category>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Category> getCategory(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getCategoryBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestParam("name") String name,
            @RequestParam("isFeatured") boolean isFeatured,
            @RequestParam(value = "parentId", required = false) Long parentId) {

        Category category = categoryService.createCategory(name, isFeatured, parentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}