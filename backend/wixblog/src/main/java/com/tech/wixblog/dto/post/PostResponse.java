package com.tech.wixblog.dto.post;

import com.tech.wixblog.dto.user.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostResponse extends BasePostDto {
    private AuthorDto author;
}