package org.atelier1.exception;

public class CannotDeleteProductException extends RuntimeException {
    public CannotDeleteProductException(String message) {
        super(message);
    }
}
