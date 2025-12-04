package com.tech.wixblog.dto.comment;

import com.tech.wixblog.dto.user.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentResponse extends BaseCommentDto {
    private AuthorDto author;
    private Long postId;
    private Long parentCommentId;
    private List<CommentResponse> replies;
    private Integer replyCount;
}