package com.tech.wixblog.controllers;

import com.tech.wixblog.domain.dto.CategoryDto;
import com.tech.wixblog.domain.dto.CreateCategoryRequest;
import com.tech.wixblog.domain.entities.Category;
import com.tech.wixblog.mappers.CategoryMapper;
import com.tech.wixblog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories( ){
        List<CategoryDto> categories = categoryService.listCategories()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
        return ResponseEntity.ok(categories);
    }


    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequest categoryRequest
            ){
        Category categoryToCreate = categoryMapper.toEntity(categoryRequest);
        Category savecCategory = categoryService.createCategory(categoryToCreate);
        return new ResponseEntity<>(
                categoryMapper.toDto(savecCategory),
                HttpStatus.CREATED
        );

    }
}
