package dev.gus.aliasify.task;

import dev.gus.aliasify.entity.UrlMapping;
import dev.gus.aliasify.repository.UrlMappingRepository;
import org.awaitility.Durations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class CleanExpiredUrlsTaskTest {

    @InjectMocks
    @MockitoSpyBean
    private CleanExpiredUrlsTask cleanExpiredUrlsTask;

    @MockitoBean
    private UrlMappingRepository repository;

    @Test
    @DisplayName("When task executes and there are no expired urls it shouldn't delete anything")
    void executeWhenExpiredUrlsDontExistShouldDeleteNothing() {
        when(repository.findByExpiresAtBefore(any())).thenReturn(List.of());
        await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            verify(cleanExpiredUrlsTask, atLeast(2)).execute();
            verify(repository, times(0)).deleteAll();
        });
    }

    @Test
    @DisplayName("When task executes and there are expired urls it should delete them")
    void executeWhenExpiredUrlsExistShouldDelete() {
        var expiredUrls = List.of(
                new UrlMapping("url1", "short1", LocalDateTime.now().minusHours(2)),
                new UrlMapping("url2", "short2", LocalDateTime.now().minusHours(3)));
        when(repository.findByExpiresAtBefore(any())).thenReturn(expiredUrls);
        await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            verify(cleanExpiredUrlsTask, atLeast(2)).execute();
            verify(repository, times(1)).deleteAll(expiredUrls);
        });
    }
}