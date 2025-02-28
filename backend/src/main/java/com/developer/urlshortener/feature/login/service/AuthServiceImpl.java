package com.developer.urlshortener.feature.login.service;

import com.developer.urlshortener.feature.login.domain.RefreshTokenDomain;
import com.developer.urlshortener.feature.login.domain.UserDomain;
import com.developer.urlshortener.feature.login.entities.RefreshTokenEntity;
import com.developer.urlshortener.feature.login.entities.UserEntity;
import com.developer.urlshortener.feature.login.repository.UserRepository;
import com.developer.urlshortener.feature.login.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService{

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenService = refreshTokenService;
    }

    public Optional<UserDomain> registerUser(UserDomain userDomain) {
        if (userRepository.existsByEmail(userDomain.getEmail())) {
            return Optional.empty();
        }


        String encodedPassword = passwordEncoder.encode(userDomain.getEmail());
        UserEntity userEntity = userDomain.toEntity(encodedPassword);
        userEntity = userRepository.save(userEntity);

        String accessToken = jwtTokenUtil.generateToken(userEntity.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(userDomain);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userEntity)
                .token(refreshToken)
                .expiryDate(LocalDateTime.now().plusDays(30))
                .build();

        userEntity.addRefreshToken(refreshTokenEntity);
        UserDomain registeredUser = new UserDomain(userEntity);
        return Optional.of(registeredUser);
    }
    public Optional<UserDomain> loginUser(String email, String password) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);

        if (optionalUserEntity.isEmpty()) {
            return Optional.empty();
        }

        UserEntity userEntity = optionalUserEntity.get();
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            return Optional.empty();
        }

        String accessToken = jwtTokenUtil.generateToken(email);
        String refreshToken = refreshTokenService.createRefreshToken(new UserDomain(userEntity));

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userEntity)
                .token(refreshToken)
                .expiryDate(LocalDateTime.now().plusDays(30))
                .build();

        userEntity.addRefreshToken(refreshTokenEntity);
        UserDomain loggedInUser = new UserDomain(userEntity);
        return Optional.of(loggedInUser);
    }
    public Optional<String> refreshAccessToken(String refreshToken) {
        Optional<RefreshTokenEntity> optionalRefreshToken = refreshTokenService.findByToken(refreshToken);

        if (optionalRefreshToken.isEmpty() || optionalRefreshToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }

        String email = optionalRefreshToken.get().getUser().getEmail();
        String accessToken = jwtTokenUtil.generateToken(email);
        return Optional.of(accessToken);
    }

}
