package com.developer.urlshortener.feature.login.service;

import com.developer.urlshortener.feature.login.domain.AuthProvider;
import com.developer.urlshortener.feature.login.domain.RefreshTokenDomain;
import com.developer.urlshortener.feature.login.domain.UserDomain;
import com.developer.urlshortener.feature.login.entities.RefreshTokenEntity;
import com.developer.urlshortener.feature.login.entities.UserEntity;
import com.developer.urlshortener.feature.login.messages.LoginUserRequest;
import com.developer.urlshortener.feature.login.messages.LoginUserResponse;
import com.developer.urlshortener.feature.login.messages.RegisterUserRequest;
import com.developer.urlshortener.feature.login.messages.RegisterUserResponse;
import com.developer.urlshortener.feature.login.repository.UserRepository;
import com.developer.urlshortener.feature.login.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Optional<RegisterUserResponse> registerUser(RegisterUserRequest request) {
        String email = request.getEmail();
        if (userRepository.existsByEmail(email)) {
            return Optional.empty();
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .provider(AuthProvider.LOCAL)
                .build();
        userEntity = userRepository.save(userEntity);

        String accessToken = jwtTokenUtil.generateToken(userEntity.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(new UserDomain(userEntity));

        UserDomain registeredUser = new UserDomain(userEntity);
        RegisterUserResponse response = new RegisterUserResponse(registeredUser, accessToken, refreshToken);
        return Optional.of(response);
    }
    public Optional<LoginUserResponse> loginUser(LoginUserRequest request) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(request.getEmail());
        if (optionalUserEntity.isEmpty()) {
            return Optional.empty();
        }
        UserDomain userDomain = new UserDomain(optionalUserEntity.get());
        if (userDomain.getAuthProvider() != AuthProvider.LOCAL ||
                !passwordEncoder.matches(request.getPassword(), userDomain.getPassword())) {
            return Optional.empty();
        }

        String accessToken = jwtTokenUtil.generateToken(request.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(userDomain);

        LoginUserResponse loggedInUser = new LoginUserResponse(userDomain, accessToken, refreshToken);
        return Optional.of(loggedInUser);
    }

    public Optional<String> refreshAccessToken(String refreshToken) {
        String accessToken = refreshTokenService.refreshAccessToken(refreshToken);
        return Optional.of(accessToken);
    }
}
