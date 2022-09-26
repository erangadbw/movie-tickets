package com.eranga.movietickets.api.advice;


import com.eranga.movietickets.api.exceptions.EmptyCustomersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String >> handleInvalidArgument(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String, String > errorMap = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(error->{
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        return new ResponseEntity<Map<String, String >>(errorMap,HttpStatus.BAD_REQUEST);

    }

}
