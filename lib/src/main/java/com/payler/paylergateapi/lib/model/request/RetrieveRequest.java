package com.payler.paylergateapi.lib.model.request;

public class RetrieveRequest extends GateRequest {

    private String key;

    private String orderId;

    private String password;

    private Long amount;

    public RetrieveRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public RetrieveRequest setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public RetrieveRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public RetrieveRequest setAmount(Long amount) {
        this.amount = amount;
        return this;
    }
}
