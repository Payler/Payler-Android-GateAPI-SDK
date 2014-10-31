package com.payler.paylergateapi.lib.model.request;

public class RefundRequest extends GateRequest {

    private String key;

    private String orderId;

    private String password;

    private Long amount;

    public RefundRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public RefundRequest setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public RefundRequest setAmount(long amount) {
        this.amount = amount;
        return this;
    }

    public RefundRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
