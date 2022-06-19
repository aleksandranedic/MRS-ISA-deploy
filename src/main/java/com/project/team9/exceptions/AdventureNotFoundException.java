package com.project.team9.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AdventureNotFoundException extends RuntimeException {
    public AdventureNotFoundException(String id) {
    }

    public AdventureNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdventureNotFoundException(Throwable cause) {
        super(cause);
    }

    public AdventureNotFoundException() {
    }
}
