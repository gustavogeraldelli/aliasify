package dev.gus.aliasify.controller;

import dev.gus.aliasify.dtos.OriginalUrlRequestDTO;
import dev.gus.aliasify.dtos.ShortUrlResponseDTO;
import dev.gus.aliasify.dtos.ShortUrlStatsResponseDTO;
import dev.gus.aliasify.entity.UrlMapping;
import dev.gus.aliasify.service.ShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ShortenerController {

    private final ShortenerService service;

    public ShortenerController(ShortenerService service) {
        this.service = service;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortUrlResponseDTO> shortenUrl(@RequestBody OriginalUrlRequestDTO originalUrlRequestDTO) {
        String originalUrl = originalUrlRequestDTO.originalUrl();
        if (originalUrl == null || originalUrl.isEmpty())
            return ResponseEntity.badRequest().build();

        UrlMapping urlMapping = service.shortenUrl(originalUrl);
        return ResponseEntity.ok(ShortUrlResponseDTO.from(urlMapping));
    }

    @GetMapping("/api/stats/{shortUrl}")
    public ResponseEntity<ShortUrlStatsResponseDTO> stats(@PathVariable String shortUrl) {
        if (shortUrl == null || shortUrl.isEmpty())
            return ResponseEntity.badRequest().build();

        Optional<UrlMapping> urlMapping = service.findByShortUrl(shortUrl);
        if (urlMapping.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(ShortUrlStatsResponseDTO.from(urlMapping.get()));
    }

}
