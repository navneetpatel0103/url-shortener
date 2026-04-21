package com.url.shortner.service.impl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.url.shortner.entity.UrlMapping;
import com.url.shortner.model.UrlRequest;
import com.url.shortner.model.UrlResponse;
import com.url.shortner.repository.UrlMappingRepository;
import com.url.shortner.service.AnalyticsService;
import com.url.shortner.service.UrlShortenerService;
import com.url.shortner.util.Base62Encoder;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;
    private final AnalyticsService analyticsService;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.url.shortener.cache-ttl-hours:24}")
    private int cacheTtlHours;

    private static final String CACHE_PREFIX = "url:";

    public UrlShortenerServiceImpl(UrlMappingRepository urlMappingRepository,
            AnalyticsService analyticsService,
            StringRedisTemplate redisTemplate) {
        this.urlMappingRepository = urlMappingRepository;
        this.analyticsService = analyticsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public UrlResponse createShortUrl(UrlRequest request) {
        String longUrl = request.getLongUrl();

        // 1. Validate URL
        if (!isValidUrl(longUrl)) {
            throw new IllegalArgumentException("Invalid URL format");
        }

        UrlMapping entity = new UrlMapping();
        entity.setLongUrl(longUrl);

        // 1. Save → get auto-generated ID
        UrlMapping saved = urlMappingRepository.save(entity);

        // 2. Generate shortKey
        String shortKey = Base62Encoder.encode(saved.getId());
        saved.setShortKey(shortKey);

        // 3. Update
        urlMappingRepository.save(saved);

        cacheUrl(shortKey, longUrl);

        return new UrlResponse(baseUrl + "/" + shortKey);
    }

    @Override
    public String getOriginalUrl(String shortKey) {
        // 1. Check Cache (High Read Optimization)
        String cachedUrl = redisTemplate.opsForValue().get(CACHE_PREFIX + shortKey);
        if (cachedUrl != null) {
            analyticsService.incrementClickCount(shortKey);
            return cachedUrl;
        }

        // 2. Fallback to DB
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new IllegalArgumentException("Short URL not found"));

        // 3. Increment analytics and Cache result
        analyticsService.incrementClickCount(shortKey);
        cacheUrl(shortKey, urlMapping.getLongUrl());

        return urlMapping.getLongUrl();
    }

    private void cacheUrl(String shortKey, String longUrl) {
        redisTemplate.opsForValue().set(CACHE_PREFIX + shortKey, longUrl, Duration.ofHours(cacheTtlHours));
    }

    private boolean isValidUrl(String url) {
        try {
            java.net.URI uri = new java.net.URI(url);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }

}
