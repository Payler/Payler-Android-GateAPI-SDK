package com.payler.paylergateapi.lib.model.request;

public class StatusRequest extends GateRequest {

    private String key;

    private String orderId;


    public StatusRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public StatusRequest setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }
}
