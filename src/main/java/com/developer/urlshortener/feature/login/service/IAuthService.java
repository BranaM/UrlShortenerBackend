package com.developer.urlshortener.feature.login.service;

import com.developer.urlshortener.feature.login.domain.UserDomain;
import com.developer.urlshortener.feature.login.messages.LoginUserRequest;
import com.developer.urlshortener.feature.login.messages.LoginUserResponse;
import com.developer.urlshortener.feature.login.messages.RegisterUserRequest;
import com.developer.urlshortener.feature.login.messages.RegisterUserResponse;

import java.util.Optional;

public interface IAuthService {
    Optional<RegisterUserResponse> registerUser(RegisterUserRequest request);
    Optional<LoginUserResponse> loginUser(LoginUserRequest request);
    Optional<String> refreshAccessToken(String refreshToken);
}
