package com.reddit.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtAuthResDto {

    private String JwtToken;
    private String userName;

    //New fields for refresh token

    private String refreshToken;
    private Instant expiresAt;

}
