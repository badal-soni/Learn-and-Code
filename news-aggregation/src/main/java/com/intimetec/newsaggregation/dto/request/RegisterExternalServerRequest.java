package com.intimetec.newsaggregation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterExternalServerRequest {

    @NotEmpty
    private String serverName;

    @NotEmpty
    private String apiKey;

    @NotEmpty
    private String apiUrl;

}
