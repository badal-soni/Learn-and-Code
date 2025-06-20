package com.intimetec.newsaggregation.dto.response;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ExternalServerStatusResponse {

    @NotEmpty
    private String serverName;
    private boolean activeStatus;
    private String lastAccessedDate;

}
