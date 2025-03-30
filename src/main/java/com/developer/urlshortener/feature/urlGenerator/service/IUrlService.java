package com.developer.urlshortener.feature.urlGenerator.service;

import com.developer.urlshortener.feature.urlGenerator.domain.UrlDomain;
import com.developer.urlshortener.feature.urlGenerator.messages.GenerateShortUrlRequest;

import java.util.List;
import java.util.Optional;

public interface IUrlService {
    String generateBase62(int length);
    String generateShortUrl();
    Optional<UrlDomain> createShortUrl(GenerateShortUrlRequest generateShortUrlRequest);
    List<UrlDomain> findAllUrls();
    Optional<UrlDomain> findById(Long id);
    Optional<UrlDomain> findByShortUrl(String shortUrl);
    void deleteUrl(Long id);
    String redirectToOriginalUrl(String shortUrl);
}
