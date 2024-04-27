package com.reddit.backend.dto;

import com.reddit.backend.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResDto implements Serializable {

    private String postId;
    private String postDescription;
    private String url;
    private String postName;
    private String userName;
    private String subredditName;

    private int voteCount;
    private int commentCount;
    private String duration;
    private Set<Comment> comments;


}
