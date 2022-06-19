package com.project.team9.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ReservationNotAvailableException extends RuntimeException{
    public ReservationNotAvailableException() {
        super();
    }

    public ReservationNotAvailableException(String message) {
        super(message);
    }

    public ReservationNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationNotAvailableException(Throwable cause) {
        super(cause);
    }
}
