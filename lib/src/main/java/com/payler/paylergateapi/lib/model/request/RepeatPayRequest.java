package com.payler.paylergateapi.lib.model.request;

public class RepeatPayRequest extends GateRequest {

    private String key;

    private String orderId;

    private Long amount;

    private String recurrentTemplateId;

    public RepeatPayRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public RepeatPayRequest setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public RepeatPayRequest setAmount(Long amount) {
        this.amount = amount;
        return this;
    }

    public RepeatPayRequest setRecurrentTemplateId(String recurrentTemplateId) {
        this.recurrentTemplateId = recurrentTemplateId;
        return this;
    }
}
