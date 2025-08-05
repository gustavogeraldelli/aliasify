package dev.gus.aliasify.task;

import dev.gus.aliasify.repository.UrlMappingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class CleanExpiredUrlsTask {

    private final UrlMappingRepository repository;

    public CleanExpiredUrlsTask(UrlMappingRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void execute() {
        var expiredUrls = repository.findByExpiresAtBefore(LocalDateTime.now());
        if (expiredUrls.isEmpty())
            return;

        repository.deleteAll(expiredUrls);
    }

}
