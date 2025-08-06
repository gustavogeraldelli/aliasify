package dev.gus.aliasify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gus.aliasify.dtos.OriginalUrlRequestDTO;
import dev.gus.aliasify.entity.UrlMapping;
import dev.gus.aliasify.service.ShortenerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortenerController.class)
class ShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShortenerService service;

    @Test
    @DisplayName("Shortening a new and valid url should return the shortened url with status OK")
    void shortenUrlWhenUrlIsNewAndValidShouldReturnShortenedUrlWithStatusOk() throws Exception {
        String originalUrl = "http://original.long/url";
        String shortUrl = "25h0RT9";
        OriginalUrlRequestDTO originalUrlRequestDTO = new OriginalUrlRequestDTO(originalUrl);
        UrlMapping urlMapping = new UrlMapping(originalUrl, shortUrl, LocalDateTime.now().plusHours(2L));
        when(service.shortenUrl(any())).thenReturn(urlMapping);

        mockMvc.perform(post("/api/shorten")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(originalUrlRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value(shortUrl));
        verify(service, times(1)).shortenUrl(any());
    }

    @Test
    @DisplayName("Shortening an invalid url should return status Bad Request")
    void shortenUrlWhenUrlIsInvalidShouldReturnStatusBadRequest() throws Exception {
        String originalUrl = "";
        OriginalUrlRequestDTO originalUrlRequestDTO = new OriginalUrlRequestDTO(originalUrl);

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalUrlRequestDTO)))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).shortenUrl(any());
    }

    @Test
    @DisplayName("Checking stats of an existing short url should return its statistics with status OK")
    void statsWhenShortUrlIsValidShouldReturnStatisticsWithStatusOk() throws Exception {
        String originalUrl = "http://original.long/url";
        String shortUrl = "25h0RT9";
        int clickCount = 10;
        UrlMapping urlMapping = new UrlMapping(originalUrl, shortUrl, LocalDateTime.now().plusHours(1L));
        urlMapping.setClickCount(clickCount);

        when(service.findByShortUrl(any())).thenReturn(Optional.of(urlMapping));

        mockMvc.perform(get("/api/stats/" + shortUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value(shortUrl))
                .andExpect(jsonPath("$.clickCount").value(clickCount));
        verify(service, times(1)).findByShortUrl(any());
    }

    @Test
    @DisplayName("Checking stats of a non existing short url should return status Not Found")
    void statsWhenShortUrlDontExistShouldReturnStatusNotFound() throws Exception {
        String shortUrl = "foundnt";

        when(service.findByShortUrl(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/stats/" + shortUrl))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findByShortUrl(any());
    }

}