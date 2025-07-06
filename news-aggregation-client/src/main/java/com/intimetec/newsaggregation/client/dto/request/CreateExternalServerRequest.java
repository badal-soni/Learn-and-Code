package com.intimetec.newsaggregation.client.dto.request;

import lombok.Data;

@Data
public class CreateExternalServerRequest {

    private String serverName;
    private String apiKey;
    private String apiUrl;

}
