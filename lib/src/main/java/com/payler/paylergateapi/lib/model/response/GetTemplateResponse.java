package com.payler.paylergateapi.lib.model.response;

/**
 * Ответ на успешный запрос получения информации о шаблоне рекуррентных платежей
 */
public class GetTemplateResponse extends Response {

    /**
     * Идентификатор шаблона рекуррентных платажей
     */
    private String recurrentTemplateId;

    /**
     * Дата и время регистрации шаблона рекуррентных платежей в системе ​Payler (yyyy­MM­dd HH:mm:ss)
     */
    private String created;

    /**
     * Имя держателя карты, к которой привязан шаблон
     */
    private String cardHolder;

    /**
     * Маскированный номер банковской карты, к которой привязан шаблон
     */
    private String cardNumber;

    /**
     * Срок действия шаблона рекуррентных платежей
     */
    private String expiry;

    /**
     * Показывет, активен ли шаблон
     */
    private Boolean active;

    public String getRecurrentTemplateId() {
        return recurrentTemplateId;
    }

    public String getCreated() {
        return created;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public Boolean isActive() {
        return active;
    }
}
