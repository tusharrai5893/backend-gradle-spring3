package com.reddit.backend.domain;

import com.reddit.backend.models.VoteType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDto {

    @Enumerated
    private VoteType voteType;
    private Long postId;

}
