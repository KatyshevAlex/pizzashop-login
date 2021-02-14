package com.pizzashop.login.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoContentFoundException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public NoContentFoundException(String message, String developerMessage) {
        super(message);
        errorResponse = new ErrorResponse(
                message,
                developerMessage,
                HttpStatus.NO_CONTENT,
                HttpStatus.NO_CONTENT.value());
    }
}