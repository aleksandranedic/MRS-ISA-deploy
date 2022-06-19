package com.project.team9.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class CannotDeleteException extends RuntimeException{
    public CannotDeleteException() {
        super();
    }

    public CannotDeleteException(String message) {
        super(message);
    }

    public CannotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotDeleteException(Throwable cause) {
        super(cause);
    }
}
