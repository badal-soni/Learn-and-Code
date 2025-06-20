package com.intimetec.newsaggregation.client;

import com.intimetec.newsaggregation.dto.response.CommonNewsApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiClient {

    private final RestTemplate restTemplate;

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        // todo: refer this: https://stackoverflow.com/questions/23205213/how-to-extract-http-status-code-from-the-resttemplate-call-to-a-url
        // todo: return ResponseEntity instead of direct response entity
//        return new ResponseEntity<>(restTemplate.getForObject(url, responseType));
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .header("content-type", "application/json")
                .build();
        return restTemplate.exchange(request, responseType);
    }

    // Overloaded method for List<CommonNewsApiResponse>
    public ResponseEntity<List<CommonNewsApiResponse>> getList(String url) {
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .header("content-type", "application/json")
                .build();
        return restTemplate.exchange(request, new ParameterizedTypeReference<List<CommonNewsApiResponse>>() {});
    }

}
