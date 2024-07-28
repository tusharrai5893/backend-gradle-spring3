package com.reddit.backend.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDto {

    private String username;

    @NotBlank
    private String refreshToken;

}
