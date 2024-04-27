package com.reddit.backend.dto;

import com.reddit.backend.models.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDto {

    private VoteType voteType;
    private Long postId;

}
