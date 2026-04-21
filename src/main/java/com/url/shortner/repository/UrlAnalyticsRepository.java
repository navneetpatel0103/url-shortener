package com.url.shortner.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.url.shortner.entity.ClickAnalytics;

@Repository
public interface UrlAnalyticsRepository extends JpaRepository<ClickAnalytics, Long> {

    Optional<ClickAnalytics> findByShortKey(String shortKey);

    @Modifying
    @Query("UPDATE ClickAnalytics c SET c.clickCount = COALESCE(c.clickCount, 0) + 1, c.lastAccessedAt = :now WHERE c.shortKey = :shortKey")
    void incrementClickCount(String shortKey, LocalDateTime now);

}