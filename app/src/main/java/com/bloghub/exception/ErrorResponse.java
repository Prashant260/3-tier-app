package com.bloghub.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter//
@Setter//generating getter and setter methods

@NoArgsConstructor //generating constructor with no arguments
public class ErrorResponse {//class to represent error response

    private int statusCode;//http status code
    private String errorMessage;//error message

    public ErrorResponse(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }


}
