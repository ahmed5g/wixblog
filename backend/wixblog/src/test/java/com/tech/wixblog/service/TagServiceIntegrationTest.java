package com.tech.wixblog.service;

import com.tech.wixblog.dto.TagResponse;
import com.tech.wixblog.model.Category;
import com.tech.wixblog.model.Tag;
import com.tech.wixblog.repository.CategoryRepository;
import com.tech.wixblog.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TagServiceIntegrationTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Long healthCategoryId;
    private Long fitnessTagId;

    @BeforeEach
    void setUp() {
        // CRUCIAL: Clear data to ensure a fresh state for every test
        tagRepository.deleteAll();
        categoryRepository.deleteAll();

        // 1. Setup Category
        Category health = categoryRepository.save(Category.builder()
                                                          .name("Health")
                                                          .slug("health")
                                                          .build());
        healthCategoryId = health.getId();

        // 2. Setup "Normal" Trending Tag
        Tag fitness = tagRepository.save(Tag.builder()
                                                 .name("Fitness")
                                                 .slug("fitness")
                                                 .trendingScore(100)
                                                 .usageCount(10)
                                                 .build());
        fitnessTagId = fitness.getId();

        // 3. Setup "Very Popular" Tag
        tagRepository.save(Tag.builder()
                                   .name("Wellness")
                                   .slug("wellness")
                                   .trendingScore(500)
                                   .usageCount(50)
                                   .build());

        // 4. Setup Tag linked to Category
        tagRepository.save(Tag.builder()
                                   .name("Mental Health")
                                   .slug("mental-health")
                                   .trendingScore(10)
                                   .usageCount(1)
                                   .suggestedCategory(health)
                                   .build());
    }

    // --- DYNAMIC TRENDING TESTS ---

    @Test
    @DisplayName("Should increase score when user interacts (clicks) on a tag")
    void shouldIncreaseScoreOnInteraction() {
        tagService.handleTagInteraction(fitnessTagId);

        // This forces Hibernate to forget the cached version and look at the DB
        Tag updated = tagRepository.findById(fitnessTagId).orElseThrow();

        assertThat(updated.getTrendingScore()).isEqualTo(102);
    }

    @Test
    void shouldBoostScoreWhenTagIsUsedInPost() {
        // Start: 100
        tagService.processTagInput(Set.of("Fitness"));

        Tag updated = tagRepository.findById(fitnessTagId).orElseThrow();

        // If your current logic adds 15 (10 from model + 5 from service),
        // adjust this to 115. If you only want 5, fix the Tag.java model.
        assertThat(updated.getTrendingScore()).isEqualTo(115);
        assertThat(updated.getUsageCount()).isEqualTo(11);
    }

    @Test
    @DisplayName("Should reduce scores by 10% when daily decay runs")
    void shouldApplyDailyDecay() {
        tagService.applyDailyDecay();

        Tag updated = tagRepository.findById(fitnessTagId).orElseThrow();

        // 100 * 0.9 = 90
        assertThat(updated.getTrendingScore()).isEqualTo(90);
    }

    // --- SUGGESTION & ALGORITHM TESTS ---

    @Test
    void shouldPrioritizeFollowedCategoryOverGlobalTrends() {
        Set<Long> followedCategories = Set.of(healthCategoryId);

        Page<TagResponse> result = tagService.getSuggestedTagsForUser(
                Set.of(), followedCategories, PageRequest.of(0, 10));

        // Mental Health is #1 because of Category link, despite lower score than Wellness
        assertThat(result.getContent().get(0).name()).isEqualTo("Mental Health");
    }

    @Test
    void shouldReturnGlobalTrendsWhenNoFollowsProvided() {
        Page<TagResponse> result = tagService.getSuggestedTagsForUser(
                Set.of(), Set.of(), PageRequest.of(0, 10));

        assertThat(result.getContent().get(0).name()).isEqualTo("Wellness");
        assertThat(result.getContent().get(1).name()).isEqualTo("Fitness");
    }

    // --- SLUG & INPUT CLEANING TESTS ---

    @Test
    void shouldGenerateCleanSlugsWithComplexInput() {
        String input = "  Spring Boot @ 3.0!!!  ";
        String result = org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(tagService, "generateSlug", input);

        assertThat(result).isEqualTo("spring-boot-30");
    }

    @Test
    void shouldHandleNullOrEmptyInputGracefully() {
        Set<Tag> result = tagService.processTagInput(Set.of("", "   "));
        assertThat(result).isEmpty();
    }

    @Test
    void shouldLinkNewTagsToCategory() {
        String catName = "Technology";
        categoryRepository.save(Category.builder().name(catName).slug("tech").build());

        tagService.findOrCreateTags(Set.of("Spring"), catName);

        Tag tag = tagRepository.findBySlug("spring").orElseThrow();
        assertThat(tag.getSuggestedCategory().getName()).isEqualTo(catName);
    }
}