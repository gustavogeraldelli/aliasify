package dev.gus.aliasify.service;

import dev.gus.aliasify.entity.UrlMapping;
import dev.gus.aliasify.repository.UrlMappingRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ShortenerService {

    private final Long hoursUntilExpiration = 2L;
    private final Integer shortUrlLength = 7;

    private final UrlMappingRepository repository;

    public ShortenerService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public UrlMapping shortenUrl(String originalUrl) {
        Optional<UrlMapping> opt = repository.findByOriginalUrl(originalUrl);
        if (opt.isPresent())
            return opt.get();

        UrlMapping urlMapping = new UrlMapping(originalUrl,
                generateShortUrl(),
                LocalDateTime.now().plusHours(hoursUntilExpiration));

        return repository.save(urlMapping);
    }

    public Optional<UrlMapping> findByShortUrl(String shortUrl) {
        return repository.findByShortUrl(shortUrl);
    }

    public Optional<UrlMapping> redirectToOriginalUrl(String shortUrl) {
        Optional<UrlMapping> opt = repository.findByShortUrl(shortUrl);
        if (opt.isEmpty())
            return Optional.empty();

        UrlMapping urlMapping = opt.get();
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        
        repository.save(urlMapping);

        return Optional.of(urlMapping);
    }

    private String generateShortUrl() {
        String shortUrl;
        do {
            shortUrl = RandomStringUtils.randomAlphanumeric(shortUrlLength);
        } while (repository.existsByShortUrl(shortUrl));
        return shortUrl;
    }
}
