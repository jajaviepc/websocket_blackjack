package com.ws.wsblackjack2.error;

public class FullTableException extends RuntimeException {
    public FullTableException(String message) {
        super(message);
    }
}
