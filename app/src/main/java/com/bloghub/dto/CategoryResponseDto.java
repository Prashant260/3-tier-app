package com.bloghub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String catName;
    private String descr;
}
