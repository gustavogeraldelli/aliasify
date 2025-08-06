package dev.gus.aliasify.service;

import dev.gus.aliasify.entity.UrlMapping;
import dev.gus.aliasify.repository.UrlMappingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortenerServiceTest {

    @Mock
    private UrlMappingRepository repository;

    @InjectMocks
    private ShortenerService service;

    @Test
    @DisplayName("Shortening a new url should create and save a short url")
    void shortenUrlWhenUrlIsNewShouldCreateAndSaveNewUrlMapping() {
        String originalUrl = "http://original.long/url";
        when(repository.findByOriginalUrl(any())).thenReturn(Optional.empty());
        when(repository.existsByShortUrl(any())).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        UrlMapping result = service.shortenUrl(originalUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(7, result.getShortUrl().length()); //improve 7 hard coded
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Shortening a URL that is already shortened should return the existing short url")
    void shortenUrlWhenUrlExistsShouldReturnExistingUrlMapping() {
        String originalUrl = "http://original.long/url";
        String shortUrl = "25h0RT9";
        UrlMapping urlMapping = new UrlMapping(originalUrl,
                shortUrl, LocalDateTime.now().plusMinutes(30));
        when(repository.findByOriginalUrl(any())).thenReturn(Optional.of(urlMapping));

        UrlMapping result = service.shortenUrl(originalUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(shortUrl, result.getShortUrl());
        verify(repository, times(0)).save(any());
    }

    @Test
    @DisplayName("Accessing a valid short url should increase its click count and return an optional of the url")
    void redirectToOriginalUrlWhenUrlExistsShouldIncrementClickCountAndReturnUrl() {
        String originalUrl = "http://original.long/url";
        String shortUrl = "25h0RT9";
        UrlMapping urlMapping = new UrlMapping(originalUrl,
                shortUrl, LocalDateTime.now().plusMinutes(30));
        when(repository.findByShortUrl(any())).thenReturn(Optional.of(urlMapping));

        Optional<UrlMapping> result = service.redirectToOriginalUrl(shortUrl);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(originalUrl, result.get().getOriginalUrl());
        assertEquals(shortUrl, result.get().getShortUrl());
        assertEquals(1, result.get().getClickCount());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Accessing a short url that doesn't exist should return an empty optional")
    void redirectToOriginalUrlWhenUrlDontExistShouldReturnEmpty() {
        String shortUrl = "25h0RT9";
        when(repository.findByShortUrl(any())).thenReturn(Optional.empty());

        Optional<UrlMapping> result = service.redirectToOriginalUrl(shortUrl);

        assertNotNull(result);
        assertFalse(result.isPresent());
        verify(repository, times(0)).save(any());
    }
}