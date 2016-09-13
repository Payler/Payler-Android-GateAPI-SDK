package com.payler.paylergateapi.lib.model.response;

/**
 * Актуальный статус платежа.
 */
public class StatusResponse extends Response {

    /**
     * Идентификатор платежа в системе Продавца.
     */
    private String orderId;

    /**
     * Сумма платежа в копейках.
     */
    private Long amount;

    /**
     * Состояние платежа.  См. статусы транзакций.
     */
    private String status;

    /**
     * Тип оплаты.
     */
    private String payment_type;

    /**
     * Присутствует, если в рамках текущей транзакции был создан шаблон рекуррентных платежей или
     * она была осуществлена по шаблону.
     */
    private String recurrentTemplateId;

    /**
     * @return Состояние платежа.
     * Cм. статусы транзакций.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @deprecated use {@link #getStatus()}
     */
    public String getPaymentType() { return status; }

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
