package dev.gus.aliasify.dtos;

import dev.gus.aliasify.entity.UrlMapping;

public record ShortUrlResponseDTO(
        String shortUrl
) {
    public static ShortUrlResponseDTO from(UrlMapping urlMapping) {
        return new ShortUrlResponseDTO(urlMapping.getShortUrl());
    }
}
