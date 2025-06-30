package com.intimetec.newsaggregation.dto.event;

import lombok.Data;

@Data
public class UserRegisteredEvent {

    private Long userId;
    private String email;
    private String username;

}
