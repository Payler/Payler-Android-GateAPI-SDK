package com.payler.paylergateapi.lib.model.response;

public class RetrieveResponse extends Response {

    String orderId;

    long newAmount;

    public String getOrderId() {
        return orderId;
    }

    public long getNewAmount() {
        return newAmount;
    }
}
