package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.TagResponse;
import com.tech.wixblog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    /**
     * Creates new tags or updates existing ones.
     * Often used by Admins or automatically during Post creation.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Set<TagResponse> createTags (
            @RequestBody Set<String> tagNames,
            @RequestParam(required = false) String categoryName) {
        return tagService.findOrCreateTags(tagNames, categoryName);
    }

    /**
     * Returns the most popular tags based on trending score.
     */
    @GetMapping("/trending")
    public List<TagResponse> getTrendingTags(@RequestParam(defaultValue = "10") int limit) {
        return tagService.getTrendingTags(limit);
    }

    /**
     * Searches for tags.
     * Automatically boosts the 'trending score' of the first result to simulate
     * "Top Result" popularity.
     */
    @GetMapping("/search")
    public List<TagResponse> searchTags(@RequestParam String q) {
        List<TagResponse> results = tagService.searchTags(q);
        if (!results.isEmpty()) {
            // Logic: If someone searches for it, it's trending!
            tagService.handleTagInteraction(results.getFirst().id());
        }
        return results;
    }

    /**
     * Gets tags suggested for a specific user based on what they follow.
     */
    @GetMapping("/suggestions")
    public Page<TagResponse> getSuggestedTags (
            @RequestParam(required = false) Set<Long> followedTagIds,
            @RequestParam(required = false) Set<Long> followedCategoryIds,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return tagService.getSuggestedTagsForUser(followedTagIds, followedCategoryIds, pageable);
    }

    /**
     * Fetches tags belonging to a specific category.
     */
    @GetMapping("/category/{categorySlug}")
    public List<TagResponse> getTagsByCategory(@PathVariable String categorySlug) {
        return tagService.getTagsByCategory(categorySlug);
    }

    /**
     * Explicitly track a tag click/interaction via ID.
     * Useful for tracking front-end "Tag Bubbles" clicks.
     */
    @PostMapping("/{id}/interact")
    @ResponseStatus(HttpStatus.OK)
    public void trackInteraction(@PathVariable Long id) {
        tagService.handleTagInteraction(id);
    }

    /**
     * Bulk delete tags (Admin only functionality).
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTags(@RequestBody List<Long> ids) {
        tagService.deleteTags(ids);
    }
}