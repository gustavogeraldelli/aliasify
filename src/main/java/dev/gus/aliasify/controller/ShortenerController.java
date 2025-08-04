package dev.gus.aliasify.controller;

import dev.gus.aliasify.dtos.OriginalUrlRequestDTO;
import dev.gus.aliasify.dtos.ShortUrlResponseDTO;
import dev.gus.aliasify.entity.UrlMapping;
import dev.gus.aliasify.service.ShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
