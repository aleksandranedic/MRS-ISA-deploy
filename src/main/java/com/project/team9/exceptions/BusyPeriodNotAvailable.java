package com.project.team9.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BusyPeriodNotAvailable extends RuntimeException{
    public BusyPeriodNotAvailable() {
        super();
    }

    public BusyPeriodNotAvailable(String message) {
        super(message);
    }

    public BusyPeriodNotAvailable(String message, Throwable cause) {
        super(message, cause);
    }

    public BusyPeriodNotAvailable(Throwable cause) {
        super(cause);
    }
}
