package com.bloghub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice//to handle exceptions globally across all controllers
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> HandleResourceNotFoundException(ResourceNotFoundException ex) {//when this exception occurs this method will be called
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());//create error response object and error code 404
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);//return response entity with error response and status code
    }

    @ExceptionHandler(value = ResourceAlreadyExitsException.class)
//when this exception occurs this method will be called
    public ResponseEntity<ErrorResponse> HandleResourceAlreadyExitsException(ResourceAlreadyExitsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());//create error response object 409
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);//return response entity with error response and status code
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    //when this exception occurs this method will be called
    public ResponseEntity<Map<String, String>> HandleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {//
        Map<String, String> errormap = new HashMap<>();//create a map to store field errors
        BindingResult bindingResult = ex.getBindingResult();//get binding result from exception
        bindingResult.getFieldErrors().forEach(fieldError -> {  // iterate through all field errors
                    errormap.put(fieldError.getField(), fieldError.getDefaultMessage());//put field name and error message in the map

                }
        );
        return new ResponseEntity<Map<String, String>>(errormap, HttpStatus.BAD_REQUEST);//return response entity with error map and status code 400
    }


    @ExceptionHandler(value = Exception.class)
//when this exception occurs this method will be called
    public ResponseEntity<ErrorResponse> HandleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());//create error response object 500
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);//return response entity with error response and status code
    }


}
