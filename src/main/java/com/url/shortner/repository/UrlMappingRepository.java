package com.url.shortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.url.shortner.entity.UrlMapping;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortKey(String shortKey);

    Optional<UrlMapping> findByLongUrl(String longUrl);

    @Query("SELECT MAX(u.id) FROM UrlMapping u")
    Long findMaxId();

}
