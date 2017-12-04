package org.iproduct.demos.spring.hellowebflux.exceptions;

public class UserDataException extends RuntimeException {
    public UserDataException(String message) {
        super(message);
    }
}
