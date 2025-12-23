package com.tech.wixblog.service;

import com.tech.wixblog.controllers.TagMapper;
import com.tech.wixblog.dto.TagResponse;
import com.tech.wixblog.model.Category;
import com.tech.wixblog.model.Tag;
import com.tech.wixblog.repository.CategoryRepository;
import com.tech.wixblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final TagMapper tagMapper; // Inject the mapper

    @Transactional
    public Set<TagResponse> findOrCreateTags(Set<String> tagNames, String categoryName) {
        // 1. Resolve category if provided
        Category category = (categoryName != null)
                ? categoryRepository.findByName(categoryName)
                : null;

        // 2. Process and map back to DTOs
        return processTagsInternal(tagNames, category).stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toSet());
    }
    @Transactional
    public Set<Tag> processTagInput(Set<String> tagNames) {
        return processTagsInternal(tagNames, null);
    }



    @Transactional(readOnly = true)
    public List<TagResponse> getTrendingTags(int limit) {
        return tagRepository.findAllByOrderByTrendingScoreDesc().stream()
                .limit(limit)
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TagResponse> searchTags(String query) {
        return tagRepository.searchTags(query).stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getTagsByCategory(String categorySlug) {
        Category category = categoryService.getCategoryBySlug(categorySlug);
        return tagRepository.findBySuggestedCategoryId(category.getId()).stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<TagResponse> getSuggestedTagsForUser(Set<Long> followedTagIds,
                                                     Set<Long> followedCategoryIds, Pageable pageable) {
        if (followedTagIds == null) followedTagIds = Set.of(-1L);
        if (followedCategoryIds == null) followedCategoryIds = Set.of(-1L);

        return tagRepository.findSuggestedTagsForUser(followedTagIds, followedCategoryIds, pageable)
                .map(tagMapper::toResponse);
    }

    @Transactional
    public void deleteTags(List<Long> ids) {
        tagRepository.deleteAllById(ids);
    }

    private String generateSlug(String name) {
        if (name == null) return "";

        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }


    @Transactional
    public void handleTagInteraction(Long tagId) {
        // Boost score by 2 for a direct interaction
        tagRepository.incrementScore(tagId, 2);
    }

    private Set<Tag> processTagsInternal(Set<String> tagNames, Category category) {
        if (tagNames == null || tagNames.isEmpty()) return new HashSet<>();

        return tagNames.stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(name -> {
                    String slug = generateSlug(name);

                    // CRUCIAL: Check if tag exists by slug first
                    return tagRepository.findBySlug(slug)
                            .map(existingTag -> {
                                // If it exists, boost usage and score
                                existingTag.incrementUsage();
                                existingTag.setTrendingScore(existingTag.getTrendingScore() + 5);

                                // Optional: Update category if the existing tag didn't have one
                                if (existingTag.getSuggestedCategory() == null && category != null) {
                                    existingTag.setSuggestedCategory(category);
                                }
                                return tagRepository.save(existingTag);
                            })
                            .orElseGet(() -> {
                                // If it doesn't exist, create a brand new one
                                return tagRepository.save(Tag.builder()
                                                                  .name(name) // Keeps original casing for display
                                                                  .slug(slug) // Unique identifier
                                                                  .usageCount(1)
                                                                  .trendingScore(20)
                                                                  .suggestedCategory(category)
                                                                  .build());
                            });
                })
                .collect(Collectors.toSet());
    }

    // 3. New: Scheduled Daily Decay (Runs at Midnight)
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void applyDailyDecay() {
        tagRepository.decayAllScores();
    }
}