package com.payler.paylergateapi.lib.model.response;

public class StatusResponse extends Response {

    private String order_id;

    private Long amount;

    private String status;

    private String recurrentTemplateId;

    public String getStatus() {
        return status;
    }
}
