package com.gatarita.games.clientmanagementsystem.exceptions;

public class DuplicateEntryException extends Exception {
    public DuplicateEntryException(String message) {
        super(message);
    }

    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}