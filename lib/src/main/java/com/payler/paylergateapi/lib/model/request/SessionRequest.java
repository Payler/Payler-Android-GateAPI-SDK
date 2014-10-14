package com.payler.paylergateapi.lib.model.request;

public class SessionRequest extends GateRequest {

    private String key;

    private Integer type;

    private String orderId;

    private Long amount;

    private String product;

    private Float total;

    private String template;

    private String lang;

    private Boolean recurrent;

    public SessionRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public SessionRequest setType(int type) {
        this.type = type;
        return this;
    }

    public SessionRequest setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public SessionRequest setAmount(long amount) {
        this.amount = amount;
        return this;
    }

    public SessionRequest setProduct(String product) {
        this.product = product;
        return this;
    }

    public SessionRequest setTotal(float total) {
        this.total = total;
        return this;
    }

    public SessionRequest setTemplate(String template) {
        this.template = template;
        return this;
    }

    public SessionRequest setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public SessionRequest setRecurrent(boolean recurrent) {
        this.recurrent = recurrent;
        return this;
    }
}
