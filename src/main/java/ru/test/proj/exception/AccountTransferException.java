package ru.test.proj.exception;

public class AccountTransferException extends RuntimeException {

    public AccountTransferException(String message, Throwable cause) {
        super(message, cause);
    }

}