package com.payler.paylergateapi.lib.model.response;

import com.payler.paylergateapi.lib.PaylerGateAPI;

public class FindSessionResponse extends Response {
    private String id;

    private String created;

    private String validThrough;

    private String type;

    private String orderId;

    private Long amount;

    private String product;

    private String currency;

    private String payPageParams;

    private String lang;

    private boolean reccurent;

    public Long getAmount() {
        return amount;
    }

    public String getCreated() {
        return created;
    }

    public String getCurrency() {
        return currency;
    }

    public String getId() {
        return id;
    }

    public String getLang() {
        return lang;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPayPageParams() {
        return payPageParams;
    }

    public String getProduct() {
        return product;
    }

    public PaylerGateAPI.SessionType getType() {
        if ("OneStep".equals(type)) return PaylerGateAPI.SessionType.ONE_STEP;
        else if ("TwoStep".equals(type)) return PaylerGateAPI.SessionType.ONE_STEP;
        return null;
    }

    public String getValidThrough() {
        return validThrough;
    }
}
