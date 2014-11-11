package com.payler.paylergateapi.lib.model.response;

/**
 * Ответ на успешный запрос старта сессии.
 */
public class SessionResponse extends Response {

    /**
     * Идентификатор платежа в системе Payler.
     */
    private String sessionId;

    /**
     * Идентификатор оплачиваемого заказа в системе Продавца.
     */
    private String orderId;

    /**
     * Сумма платежа в копейках.
     */
    private Long amount;

    public String getSessionId() {
        return sessionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Long getAmount() {
        return amount;
    }
}
