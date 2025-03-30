package com.developer.urlshortener.feature.urlGenerator.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GenerateShortUrlRequest {
    private String originalUrl;
}
