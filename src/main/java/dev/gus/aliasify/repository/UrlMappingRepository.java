package dev.gus.aliasify.repository;

import dev.gus.aliasify.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByOriginalUrl(String originalUrl);

    boolean existsByShortUrl(String shortUrl);

    Optional<UrlMapping> findByShortUrl(String shortUrl);

    List<UrlMapping> findByExpiresAtBefore(LocalDateTime now);
}
