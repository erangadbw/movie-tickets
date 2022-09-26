package com.eranga.movietickets.api.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmptyCustomersException extends  RuntimeException{
    private  String errorCode;
    private  String errorMessage;
}

