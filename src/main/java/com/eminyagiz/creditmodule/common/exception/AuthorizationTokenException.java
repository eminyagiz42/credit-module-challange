package com.eminyagiz.creditmodule.common.exception;

public class AuthorizationTokenException extends RuntimeException {
    public AuthorizationTokenException(String string) {
        super(string);
    }

    public AuthorizationTokenException(String string, Exception e) {
        super(string, e);
    }
}
