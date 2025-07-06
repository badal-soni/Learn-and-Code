package com.intimetec.newsaggregation.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInResponse {

    private String jwtToken;
    private Set<String> roles;

}
