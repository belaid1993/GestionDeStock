package com.belaid.gestionDeStock.exception;

import lombok.Getter;

public class InvalidOperationException extends RuntimeException {

    @Getter
    private ErrorCodes errorCodes;

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOperationException(String message, Throwable cause, ErrorCodes errorCodes) {
        super(message, cause);
        this.errorCodes = errorCodes;
    }

    public InvalidOperationException(String message, ErrorCodes errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }
}
