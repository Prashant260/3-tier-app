package com.bloghub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryUpdateDto {

    private String catName;//
    private String descr;

}
