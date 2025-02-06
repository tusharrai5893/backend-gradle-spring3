package com.reddit.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtAuthResDto {

    private String JwtToken;
    private String userName;

    //New fields for refresh token

    private String refreshToken;
    private Date expiresAt;

}
