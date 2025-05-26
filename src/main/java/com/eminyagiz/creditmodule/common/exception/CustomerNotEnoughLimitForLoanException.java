package com.eminyagiz.creditmodule.common.exception;

public class CustomerNotEnoughLimitForLoanException extends RuntimeException {
    public CustomerNotEnoughLimitForLoanException(String string) {
        super(string);
    }
}
