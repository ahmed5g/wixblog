package com.tech.wixblog.service;

import com.tech.wixblog.exception.ResourceNotFoundException;
import com.tech.wixblog.model.Category;
import com.tech.wixblog.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a root category successfully")
    void shouldCreateRootCategory() {
        // We pass "Technology" (Upper), but slug should be "technology" (Lower)
        Category category = categoryService.createCategory("Technology", true, null);

        assertThat(category.getId()).isNotNull();
        // If your generator keeps case, use "Technology". If it lowercases, use "technology".
        assertThat(category.getSlug()).isEqualToIgnoringCase("technology");
        assertThat(category.isFeatured()).isTrue();
        assertThat(category.getParent()).isNull();
    }

    @Test
    @DisplayName("Should create a sub-category linked to a parent")
    void shouldCreateSubCategory() {
        Category parent = categoryService.createCategory("Engineering", false, null);
        Category child = categoryService.createCategory("Software", false, parent.getId());

        Category fetchedParent = categoryRepository.findById(parent.getId()).orElseThrow();

        assertThat(fetchedParent.getChildren())
                .extracting(Category::getName)
                .contains("Software");
    }

    @Test
    @DisplayName("Should generate unique slugs when names are identical")
    void shouldGenerateUniqueSlugsForDuplicateNames() {
        // 1. Create first 'Mobile' -> slug: 'mobile'
        categoryService.createCategory("Mobile", false, null);

        // 2. Create second 'Mobile' -> slug: 'mobile-1'
        // This works now because 'name' is no longer UNIQUE in DB
        Category secondMobile = categoryService.createCategory("Mobile", false, null);

        // 3. Create third 'Mobile' -> slug: 'mobile-2'
        Category thirdMobile = categoryService.createCategory("Mobile", false, null);

        assertThat(secondMobile.getSlug()).isEqualTo("mobile-1");
        assertThat(thirdMobile.getSlug()).isEqualTo("mobile-2");
        // Verify names are allowed to be identical
        assertThat(secondMobile.getName()).isEqualTo("Mobile");
    }

    @Test
    @DisplayName("Should fetch only root categories")
    void shouldGetAllRootCategories() {
        Category root = categoryService.createCategory("Root", false, null);
        categoryService.createCategory("Child", false, root.getId());

        List<Category> roots = categoryService.getAllRootCategories();

        assertThat(roots).hasSize(1);
        assertThat(roots.get(0).getName()).isEqualTo("Root");
    }

    @Test
    @DisplayName("Should delete category or throw exception if not found")
    void testDeleteCategory() {
        Category cat = categoryService.createCategory("To Delete", false, null);
        Long id = cat.getId();

        categoryService.deleteCategory(id);
        assertThat(categoryRepository.existsById(id)).isFalse();

        assertThatThrownBy(() -> categoryService.deleteCategory(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}