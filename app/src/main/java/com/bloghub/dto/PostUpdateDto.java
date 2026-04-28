package com.bloghub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostUpdateDto {

    private String title;
    private String content;
    private Long categoryId;
    private Long authorId;
}
