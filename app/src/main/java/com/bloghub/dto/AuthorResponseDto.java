package com.bloghub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorResponseDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String about;
}
