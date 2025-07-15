package com.intimetec.newsaggregation.client.dto.request;

import lombok.Data;

@Data
public class UpdateExternalServerRequest {

    private long serverId;
    private boolean enabled;
    private String apiKeyToUpdate;

}
