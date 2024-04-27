package com.reddit.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReqDto {

    private Long postId;
    private String subredditName;
    private String postName;
    private String url;
    private String postDescription;


}
