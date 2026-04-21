package com.url.shortner.service;

import com.url.shortner.model.UrlRequest;
import com.url.shortner.model.UrlResponse;

public interface UrlShortenerService {

    UrlResponse createShortUrl(UrlRequest request);

    String getOriginalUrl(String shortKey);

}
