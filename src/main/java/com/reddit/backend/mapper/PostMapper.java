package com.reddit.backend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.reddit.backend.dto.PostReqDto;
import com.reddit.backend.dto.PostResDto;
import com.reddit.backend.models.Comment;
import com.reddit.backend.models.Post;
import com.reddit.backend.models.Subreddit;
import com.reddit.backend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Mapping(target = "user", source = "currentUser")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subredditName")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post mapDTOtoModel(PostReqDto postReqDto, Subreddit subredditName, User currentUser);

    @Mapping(target = "subredditName", source = "post.subreddit.subredditName")
    @Mapping(target = "userName", source = "post.user.username")
    @Mapping(target = "voteCount", source = "post.voteCount")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(post))")
    @Mapping(target = "duration", expression = "java(getPostDuration(post))")
    @Mapping(target = "comments", expression = "java(post.getComments())")
    public abstract PostResDto mapModelToDto(Post post);

    public int getCommentCount(Post post) {
        Set<Comment> comments = post.getComments();
        return comments != null ? comments.size() : 0;
    }

    public String getPostDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
