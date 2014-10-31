package com.payler.paylergateapi.lib.model.response;

public class MoneyResponse extends Response {

    private String orderId;

    private Long amount;

    public String getOrderId() {
        return orderId;
    }

    public Long getAmount() {
        return amount;
    }

}
