package com.intimetec.newsaggregation.dto.response;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ExternalServerStatusResponse {

    @NotEmpty
    private String serverName;
    private Long serverId;
    private boolean activeStatus;
    private String apiKey;
    private LocalDate lastAccessedDate;
    private LocalDateTime lastFailedTime;

}
