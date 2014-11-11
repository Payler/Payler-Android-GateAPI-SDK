package com.payler.paylergateapi.lib.model.response;

/**
 * Успешный ответ на операцию Retrieve
 */
public class RetrieveResponse extends Response {

    /**
     * Идентификатор оплачиваемого заказа в системе Продавца.
     */
    String orderId;

    /**
     * Новая величина суммы платежа в копейках.
     */
    long newAmount;

    public String getOrderId() {
        return orderId;
    }

    public long getNewAmount() {
        return newAmount;
    }
}
