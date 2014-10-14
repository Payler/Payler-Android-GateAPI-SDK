package com.payler.paylergateapi.lib.model;

public class ConnectionException extends Exception {

    private static final String MESSAGE = "Connection was aborted or network problem";

    public ConnectionException() {
        super(MESSAGE);
    }

}
