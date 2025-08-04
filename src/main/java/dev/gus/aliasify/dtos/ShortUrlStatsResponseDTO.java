package dev.gus.aliasify.dtos;

import dev.gus.aliasify.entity.UrlMapping;

import java.time.LocalDateTime;

public record ShortUrlStatsResponseDTO(
        String shortUrl,
        LocalDateTime expiresAt,
        Integer clickCount
) {
    public static ShortUrlStatsResponseDTO from(UrlMapping urlMapping) {
        return new ShortUrlStatsResponseDTO(urlMapping.getShortUrl(),
                urlMapping.getExpiresAt(), urlMapping.getClickCount());
    }
}
