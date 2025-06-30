package com.intimetec.newsaggregation.client.dto.response;

import lombok.Data;

@Data
public class ViewExternalServerResponse {

    private String serverName;
    private boolean activeStatus;
    private String lastAccessedDate;
    private String apiKey;

    public String viewExternalServerDetail() {
        String activeStatusString = activeStatus ? "Active" : "Not Active";
        return serverName + " - " + activeStatusString + " - " + "last accessed: " + lastAccessedDate;
    }

    public String viewApiKey() {
        return serverName + " - " + apiKey;
    }

}
