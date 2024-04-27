package com.reddit.backend.mapper;

import com.reddit.backend.dto.SubredditDto;
import com.reddit.backend.models.Post;
import com.reddit.backend.models.Subreddit;
import com.reddit.backend.models.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.TreeSet;


@Mapper(componentModel = "spring")
public interface SubredditMapper {
    @Mapping(target = "NoOfPosts", expression = "java(postList(subreddit.getPosts()))")
    @Mapping(target = "loggedInUser", source = "currentUser")
    SubredditDto mapModelToDTO(Subreddit subreddit, String currentUser);

    default Set<Post> postList(Set<Post> posts) {
        Set<Post> p = new TreeSet<>();
        p.addAll(posts);
        return p;
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "currentUser")
    Subreddit mapDTOToModel(SubredditDto subredditDto, User currentUser);


}

