package com.intimetec.newsaggregation.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record SignInResponse(
        @NotNull String jwtToken,
        @NotNull Set<String> roles
) {

}