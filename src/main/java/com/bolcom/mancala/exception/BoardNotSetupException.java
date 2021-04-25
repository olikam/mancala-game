package com.bolcom.mancala.exception;

public class BoardNotSetupException extends RuntimeException {
    public BoardNotSetupException(String message) {
        super(message);
    }
}
