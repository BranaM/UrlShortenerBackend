package com.developer.urlshortener.feature.login.domain;

import com.developer.urlshortener.feature.login.entities.RefreshTokenEntity;
import com.developer.urlshortener.feature.login.entities.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDomain {
    private Long id;
    private String token;
    private LocalDateTime expiryDate;
    public RefreshTokenDomain(RefreshTokenEntity entity) {
        this.id = entity.getId();
        this.token = entity.getToken();
        this.expiryDate = entity.getExpiryDate();
    }

    public RefreshTokenEntity toEntity(UserEntity userEntity) {
        return RefreshTokenEntity.builder()
                .user(userEntity)
                .token(this.token)
                .expiryDate(this.expiryDate)
                .build();
    }

}
