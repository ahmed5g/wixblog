package com.tech.wixblog.mappers;

import com.tech.wixblog.domain.PostStatus;
import com.tech.wixblog.domain.dto.CategoryDto;
import com.tech.wixblog.domain.dto.CreateCategoryRequest;
import com.tech.wixblog.domain.entities.Category;
import com.tech.wixblog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts",qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);


    Category toEntity(CreateCategoryRequest categoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts){
        if (null==posts){
            return 0;
        }
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
