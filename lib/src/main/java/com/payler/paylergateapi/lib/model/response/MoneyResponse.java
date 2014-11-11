package com.payler.paylergateapi.lib.model.response;

/**
 * Успешный ответ на операции Charge, Refund
 */
public class MoneyResponse extends Response {

    /**
     * Идентификатор заказа в системе Продавца.
     */
    private String orderId;

    /**
     * Сумма платежа в копейках.
     */
    private Long amount;

    public String getOrderId() {
        return orderId;
    }

    public Long getAmount() {
        return amount;
    }

}
