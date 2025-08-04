package dev.gus.aliasify.repository;

import dev.gus.aliasify.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
}
