package com.intimetec.newsaggregation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateExternalServerDetailsRequest {

    @Positive
    private long serverId;

    private boolean enabled;

    @NotBlank
    private String apiKeyToUpdate;

}
