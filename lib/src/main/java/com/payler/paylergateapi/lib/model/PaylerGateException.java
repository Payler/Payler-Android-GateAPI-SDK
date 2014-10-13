package com.payler.paylergateapi.lib.model;

public class PaylerGateException extends Exception {

    private int mCode = 0;

    public PaylerGateException(int code, String message) {
        super(message);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }
}
