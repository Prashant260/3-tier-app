package com.bloghub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorRequestDto {
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 1, message = "About section must be at least 1 characters long")
    private String about;
}
