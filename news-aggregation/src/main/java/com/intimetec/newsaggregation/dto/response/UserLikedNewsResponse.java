package com.intimetec.newsaggregation.dto.response;

import lombok.Data;

@Data
public class UserLikedNewsResponse {

    private Long newsId;
    private boolean isLiked;

}
