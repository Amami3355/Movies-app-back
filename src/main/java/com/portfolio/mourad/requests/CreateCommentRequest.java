package com.portfolio.mourad.requests;

import lombok.Data;

@Data
public class CreateCommentRequest {

    private Integer movieId;
//    private Integer userId;
    private String text;

}
