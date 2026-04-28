package com.bloghub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryRequestDto {
    @NotBlank(message = "Category description is required")
    private String descr;

    @NotBlank(message = "Category name is required")
    private String catName;
}
