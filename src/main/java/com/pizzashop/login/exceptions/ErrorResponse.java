package com.pizzashop.login.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ErrorResponse implements Serializable {
    private final String errorMsg;
    private final String developerMsg;
    private final HttpStatus responseStatus;
    private final int responseCode;
}
