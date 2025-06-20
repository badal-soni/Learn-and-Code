package com.intimetec.newsaggregation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class UpdateNotificationPreferencesRequest {

    // todo: add validation that each category can be added only once
    public record NotificationPreference(
            @NotBlank @Pattern(regexp = "^[a-z]+$") String newsCategoryName,
            boolean shouldEnableNotification
    ) {
    }

    @NotEmpty
    private List<@NotNull NotificationPreference> preferences;

}
