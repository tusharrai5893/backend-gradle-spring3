package com.reddit.backend.dto;

import com.reddit.backend.models.Post;
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
public class SubredditDto implements Serializable {

    private long subredditId;
    private String subredditName;
    private String subredditDescription;
    private Set<Post> NoOfPosts;
    private String loggedInUser;

}
