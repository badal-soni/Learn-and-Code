package com.intimetec.newsaggregation.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiClient {

    private final RestTemplate restTemplate;

    public <Res> Res get(String url, Class<Res> responseType) {
        try {
            log.info("Making GET request to the URL: {}", url);
            return restTemplate.exchange(url, HttpMethod.GET, null, responseType).getBody();
        } catch (Exception exception) {
            log.error("Error while making call GET request to the uri {}", url);
            return null;
        }
    }

    public <Req, Res> Res post(String url, Req requestBody, Class<Res> responseType) {
        try {
            final RequestEntity<Req> requestEntity = RequestEntity
                    .post(url)
                    .body(requestBody);
            log.info("Making POST request to the URL: {} with body: {}", url, requestEntity);
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType).getBody();
        } catch (Exception exception) {
            log.error("Error while making call POST request to the uri {}", url);
            return null;
        }
    }

}
