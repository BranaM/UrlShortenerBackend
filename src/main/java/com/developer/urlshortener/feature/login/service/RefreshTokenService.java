package com.developer.urlshortener.feature.login.service;

import com.developer.urlshortener.feature.login.domain.RefreshTokenDomain;
import com.developer.urlshortener.feature.login.domain.UserDomain;
import com.developer.urlshortener.feature.login.entities.RefreshTokenEntity;
import com.developer.urlshortener.feature.login.entities.UserEntity;
import com.developer.urlshortener.feature.login.repository.RefreshTokenRepository;
import com.developer.urlshortener.feature.login.security.JwtTokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public String createRefreshToken(UserDomain userDomain) {
        refreshTokenRepository.deleteByUserId(userDomain.getId());

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .userId(userDomain.getId())
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

    public Optional<RefreshTokenDomain> findByToken(String token) {
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByToken(token);
        if(refreshTokenEntity.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(new RefreshTokenDomain(refreshTokenEntity.get()));
    }
}
