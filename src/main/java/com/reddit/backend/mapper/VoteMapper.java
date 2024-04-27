package com.reddit.backend.mapper;

import com.reddit.backend.dto.VoteDto;
import com.reddit.backend.models.Post;
import com.reddit.backend.models.User;
import com.reddit.backend.models.Vote;
import com.reddit.backend.models.VoteType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public interface VoteMapper {

    @Mapping(target = "voteId", ignore = true)
    @Mapping(target = "voteType", expression = "java(getVoteType(voteDto))")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    Vote mapDtoToModel(VoteDto voteDto, Post post, User user);

    default VoteType getVoteType(VoteDto voteDto) {
        System.out.print(voteDto.getVoteType());
        return voteDto.getVoteType();
    }

}
