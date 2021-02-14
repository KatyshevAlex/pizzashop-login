package com.pizzashop.login.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorisedException extends RuntimeException {
    private ErrorResponse errorResponse;

    public UnauthorisedException(String message, String developerMessage) {
        super(message);
        errorResponse = new ErrorResponse(
                message,
                developerMessage,
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.value());
    }
}

