package com.reddit.backend.repository;

import com.reddit.backend.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByrToken(String refreshToken);

    Optional<RefreshToken> deleteByrToken(String refreshToken);
}
