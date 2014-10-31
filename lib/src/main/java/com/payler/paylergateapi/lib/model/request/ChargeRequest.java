package com.payler.paylergateapi.lib.model.request;

public class ChargeRequest extends GateRequest {

    private String key;

    private String password;

    private String orderId;

    private Long amount;

    public ChargeRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public ChargeRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public ChargeRequest setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public ChargeRequest setAmount(Long amount) {
        this.amount = amount;
        return this;
    }
}
