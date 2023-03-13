package com.portfolio.mourad.payload.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CommentPayload {
    private Integer id;
    private String text;
    private String owner;
    private Integer movieId;
    private String imageData;
    private OffsetDateTime date;
}
