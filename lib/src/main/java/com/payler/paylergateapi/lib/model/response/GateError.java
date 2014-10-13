package com.payler.paylergateapi.lib.model.response;

public class GateError extends Response {

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
