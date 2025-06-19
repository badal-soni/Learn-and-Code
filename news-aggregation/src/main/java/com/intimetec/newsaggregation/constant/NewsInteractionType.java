package com.intimetec.newsaggregation.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewsInteractionType {

    LIKE("Liked"),
    DISLIKE("Disliked");

    private final String interactionType;

}
