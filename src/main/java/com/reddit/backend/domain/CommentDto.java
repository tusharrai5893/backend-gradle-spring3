package com.reddit.backend.domain;

import com.reddit.backend.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentDto implements Serializable {

    private Long commentId;
    private Long postId;
    private String createdDate;
    private String commentText;
    private String commentedUsername;
    private Set<Comment> comments;

}
