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
    private String password;
    private AuthProvider authProvider;

    public UserDomain(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.authProvider = userEntity.getProvider();
    }

    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(this.id);
        userEntity.setEmail(this.email);
        userEntity.setPassword(this.password);
        userEntity.setProvider(this.authProvider);

        return userEntity;
    }

}
