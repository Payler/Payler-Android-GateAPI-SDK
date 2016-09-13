package com.payler.paylergateapi.lib.model.request;

/**
 * Поиск платёжной сессии по идентификатору платежа (order_id).
 */
public class FindSessionRequest extends GateRequest {
    private String key;

    private String orderId;

    public FindSessionRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public FindSessionRequest setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }
}
