package com.developer.urlshortener.feature.login.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginUserRequest {
    private String email;
    private String password;
}
