package com.madhav.payloadgeneration.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class HttpHeaderService {
    public HttpHeaders setHeaders(String authorization, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.set("Accept", "application/json");
        headers.set("Accept-Language", "en-GB");

        return headers;
    }
}
