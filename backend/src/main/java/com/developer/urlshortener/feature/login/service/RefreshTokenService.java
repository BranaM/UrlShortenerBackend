package com.developer.urlshortener.feature.login.service;

import com.developer.urlshortener.feature.login.domain.UserDomain;
import com.developer.urlshortener.feature.login.entities.RefreshTokenEntity;
import com.developer.urlshortener.feature.login.entities.UserEntity;
import com.developer.urlshortener.feature.login.repository.RefreshTokenRepository;
import com.developer.urlshortener.feature.login.security.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtTokenUtil jwtTokenUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String createRefreshToken(UserDomain userDomain) {
        UserEntity userEntity = userDomain.toEntity("default_password");
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(userEntity)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(rt -> rt.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    public String refreshAccessToken(String refreshToken) {
        if (validateRefreshToken(refreshToken)) {
            String email = jwtTokenUtil.extractUsername(refreshToken);
            return jwtTokenUtil.generateToken(email);
        }
        throw new RuntimeException("Invalid refresh token");
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
