package com.developer.urlshortener.feature.login.rest;

import com.developer.urlshortener.feature.login.domain.UserDomain;
import com.developer.urlshortener.feature.login.messages.LoginUserRequest;
import com.developer.urlshortener.feature.login.messages.LoginUserResponse;
import com.developer.urlshortener.feature.login.messages.RegisterUserRequest;
import com.developer.urlshortener.feature.login.messages.RegisterUserResponse;
import com.developer.urlshortener.feature.login.service.AuthServiceImpl;
import com.developer.urlshortener.feature.login.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest request) {
        Optional<RegisterUserResponse> registeredUser = authService.registerUser(request);
        return registeredUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserRequest request) {
        Optional<LoginUserResponse> loggedInUser = authService.loginUser(request);
        return loggedInUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAccessToken(@RequestParam String refreshToken) {
        return authService.refreshAccessToken(refreshToken)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
