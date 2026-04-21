package com.url.shortner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortner.model.UrlRequest;
import com.url.shortner.model.UrlResponse;
import com.url.shortner.service.UrlShortenerService;
import java.net.URI;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/api/v1/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlShortenerService.createShortUrl(request));
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey) {
        String longUrl = urlShortenerService.getOriginalUrl(shortKey);

        return ResponseEntity.status(HttpStatus.FOUND) // 302
                .location(URI.create(longUrl))
                .build();
    }

}
