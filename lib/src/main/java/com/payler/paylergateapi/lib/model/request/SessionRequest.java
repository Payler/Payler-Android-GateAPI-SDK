package com.payler.paylergateapi.lib.model.request;

import java.util.HashMap;

public class SessionRequest extends GateRequest {

    private String key;

    private Integer type;

    private String orderId;

    private String currency;

    private Long amount;

    private String product;

    private Float total;

    private String template;

    private String lang;

    private String userdata;

    private Boolean recurrent;

    private HashMap<String, String> payPageParams = new HashMap<String, String>();

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

    /**
     * Валюта платежа (RUB, USD, EUR)
     *
     * @param currency Необязателен. Строка. Если не задан, то RUB.
     */
    public SessionRequest setCurrency(String currency) {
        this.currency = currency;
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

    public SessionRequest addPayPageParam(String name, String value) {
        this.payPageParams.put(name, value);
        return this;
    }

    /**
     * Пользовательские данные.
     * Можно передать в этой строке любую информацию, которую  нужно сохранить вместе с платежом.
     *
     * @param userdata Необязателен. Строка (максимум 10 KiB).
     */
    public SessionRequest setUserdata(String userdata) {
        this.userdata = userdata;
        return this;
    }

    public SessionRequest setRecurrent(boolean recurrent) {
        this.recurrent = recurrent;
        return this;
    }
}
