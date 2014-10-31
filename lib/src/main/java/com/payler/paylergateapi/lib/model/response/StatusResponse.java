package com.payler.paylergateapi.lib.model.response;

public class StatusResponse extends Response {

    private String orderId;

    private Long amount;

    private String status;

    private String recurrentTemplateId;

    public String getStatus() {
        return status;
    }

    public String getOrderId() {
        return orderId;
    }

    public Long getAmount() {
        return amount;
    }

    public String getRecurrentTemplateId() {
        return recurrentTemplateId;
    }
}
