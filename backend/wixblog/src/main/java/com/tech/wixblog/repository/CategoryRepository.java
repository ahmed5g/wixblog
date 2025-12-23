package com.tech.wixblog.repository;

import com.tech.wixblog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findBySlug(String slug);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL")
    List<Category> findRootCategoriesWithChildren();

    // Finds top-level categories sorted by name
    List<Category> findByParentIsNullOrderByNameAsc();

    @Query("SELECT c FROM Category c WHERE c.parent.slug = :slug")
    List<Category> findChildrenBySlug(@Param("slug") String slug);
    boolean existsBySlug(String slug);
    
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.featured = true ")
    List<Category> findFeaturedRootCategories();



    // Popular categories - simplified
    @Query("SELECT c FROM Category c " +
            "WHERE c.featured = true " )
    List<Category> findPopularCategories(@Param("limit") int limit);

    // Categories with most posts
    @Query("SELECT c FROM Category c " +
            "ORDER BY (SELECT COUNT(p) FROM Post p WHERE p.category = c AND p.status = 'PUBLISHED') DESC")
    List<Category> findCategoriesByPostCount (Pageable pageable);

    Category findByName (String name);

    Boolean existsByName(String name);
}