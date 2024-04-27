package com.reddit.backend.service;

import com.reddit.backend.exceptions.RedditCustomException;
import com.reddit.backend.models.RefreshToken;
import com.reddit.backend.repository.RefreshTokenRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;

    public RefreshToken generateRefreshToken() {

        RefreshToken refreshToken = RefreshToken.builder()
                .rToken(UUID.randomUUID().toString())
                .createdDate(Instant.now())
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    void validateRefreshToken(String refreshToken) {
        refreshTokenRepo.findByrToken(refreshToken)
                .orElseThrow(() -> new RedditCustomException("Refresh token not found"));


    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepo.deleteByrToken(refreshToken)
                .orElseThrow(() -> new RedditCustomException("Refresh Token not deleted"));
    }
}
