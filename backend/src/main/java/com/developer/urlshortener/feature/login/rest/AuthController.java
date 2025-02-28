package com.developer.urlshortener.feature.login.rest;

import com.developer.urlshortener.feature.login.domain.UserDomain;
import com.developer.urlshortener.feature.login.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<UserDomain> registerUser(@RequestBody UserDomain userDomain) {
        Optional<UserDomain> registeredUser = authService.registerUser(userDomain);
        return registeredUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public ResponseEntity<UserDomain> loginUser(@RequestParam String email, @RequestParam String password) {
        Optional<UserDomain> loggedInUser = authService.loginUser(email, password);
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
