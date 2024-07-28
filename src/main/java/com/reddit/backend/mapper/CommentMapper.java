package com.reddit.backend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.reddit.backend.domain.CommentDto;
import com.reddit.backend.models.Comment;
import com.reddit.backend.models.Post;
import com.reddit.backend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Comment mapDTOtoModel(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "commentedUsername", expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "comments", expression = "java(comment.getChildren() != null ? comment.getChildren() : null)")
    @Mapping(target = "createdDate", expression = "java(getPostDuration(comment))")
    CommentDto mapModelToDto(Comment comment);

    default String getPostDuration(Comment comment) {
        return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
    }


}
