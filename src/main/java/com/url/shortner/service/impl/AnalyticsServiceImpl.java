package com.url.shortner.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.url.shortner.entity.ClickAnalytics;
import com.url.shortner.repository.UrlAnalyticsRepository;
import com.url.shortner.service.AnalyticsService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UrlAnalyticsRepository urlAnalyticsRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String CLICK_COUNT_PREFIX = "clicks:";

    public AnalyticsServiceImpl(UrlAnalyticsRepository urlAnalyticsRepository, StringRedisTemplate redisTemplate) {
        this.urlAnalyticsRepository = urlAnalyticsRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Async
    public void incrementClickCount(String shortKey) {
        // High Read Optimization: Increment in Redis (Atomic & Fast)
        Long count = redisTemplate.opsForValue().increment(CLICK_COUNT_PREFIX + shortKey);
        if (count != null && count == 1) {
            redisTemplate.expire(CLICK_COUNT_PREFIX + shortKey, Duration.ofHours(24));
        }
    }

    /**
     * Periodically sync click counts from Redis to DB.
     * Runs every 5 minutes (configurable).
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    @Transactional
    public void syncClickCountsToDb() {
        log.info("Starting sync of click counts from Redis to DB...");
        // In production, replace KEYS with SCAN to avoid blocking Redis.
        Set<String> keys = redisTemplate.keys(CLICK_COUNT_PREFIX + "*");
        if (keys == null || keys.isEmpty())
            return;

        for (String key : keys) {
            String shortKey = key.substring(CLICK_COUNT_PREFIX.length());
            String countStr = redisTemplate.opsForValue().getAndSet(key, "0");

            if (countStr != null) {
                Long clicks = Long.parseLong(countStr);
                updateDb(shortKey, clicks);
            }
            // Currently scheduler runs in a single instance.
            // In distributed setup, I would replace DB update with atomic query or use
            // distributed lock.”
        }
        log.info("Click counts sync completed.");
    }

    private void updateDb(String shortKey, Long clicksToAdd) {
        if (clicksToAdd == 0) {
            return;
        }

        urlAnalyticsRepository.findByShortKey(shortKey).ifPresentOrElse(
                analytics -> {
                    analytics.setClickCount(analytics.getClickCount() + clicksToAdd);
                    analytics.setLastAccessedAt(LocalDateTime.now());
                    urlAnalyticsRepository.save(analytics);
                },
                () -> {
                    ClickAnalytics analytics = new ClickAnalytics();
                    analytics.setShortKey(shortKey);
                    analytics.setClickCount(clicksToAdd);
                    analytics.setLastAccessedAt(LocalDateTime.now());
                    urlAnalyticsRepository.save(analytics);
                });
    }

}
