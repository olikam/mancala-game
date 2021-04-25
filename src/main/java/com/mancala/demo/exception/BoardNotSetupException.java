package com.mancala.demo.exception;

public class BoardNotSetupException extends RuntimeException {
    public BoardNotSetupException(String message) {
        super(message);
    }
}
