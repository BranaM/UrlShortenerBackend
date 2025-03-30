package com.developer.urlshortener.feature.login.messages;

import com.developer.urlshortener.feature.login.domain.UserDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterUserResponse {
    private UserDomain userDomain;
    private String accessToken;
    private String refreshToken;
}
