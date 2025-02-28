package com.developer.urlshortener.feature.login.domain;

import com.developer.urlshortener.feature.login.entities.UserEntity;
import com.developer.urlshortener.feature.login.entities.RefreshTokenEntity; // prilagodi putanju
import lombok.*;


import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDomain {

    private Long id;
    private String email;
    private AuthProvider authProvider;
    private List<RefreshTokenDomain> refreshTokens;

    public UserDomain(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.authProvider = userEntity.getProvider();

        this.refreshTokens = userEntity.getRefreshTokens().stream()
                .map(RefreshTokenDomain::new)
                .collect(Collectors.toList());
    }

    public UserEntity toEntity(String encodedPassword) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(this.id);
        userEntity.setEmail(this.email);
        userEntity.setProvider(this.authProvider);
        userEntity.setPassword(encodedPassword);

        if (refreshTokens != null) {
            refreshTokens.forEach(tokenDomain -> {
                RefreshTokenEntity refreshTokenEntity = tokenDomain.toEntity(userEntity);
                userEntity.addRefreshToken(refreshTokenEntity);
            });
        }

        return userEntity;
    }

}
