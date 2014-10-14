package com.payler.paylergateapi.lib.model.response;

public class SessionResponse extends Response {

    private String sessionId;

    private String orderId;

    private Long amount;

    public String getSessionId() {
        return sessionId;
    }
}
