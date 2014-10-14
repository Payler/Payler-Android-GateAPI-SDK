package com.payler.paylergateapi.lib.model.response;

public class GateError extends Response {

    private Error error;

    public Integer getCode() {
        return error.code;
    }

    public String getMessage() {
        return error.message;
    }

    public static class Error {
        private Integer code;

        private String message;
    }

}
