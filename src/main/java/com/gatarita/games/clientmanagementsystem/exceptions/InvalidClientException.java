package com.gatarita.games.clientmanagementsystem.exceptions;

public class InvalidClientException extends Exception {
    public InvalidClientException(String message) {
        super(message);
    }

    public InvalidClientException(String message, Throwable cause) {
        super(message, cause);
    }
}