package com.payler.paylergateapi.lib.model.response;

public class ActivateTemplateResponse extends Response {
    String recurrentTemplateId;

    String created;

    String cardHolder;

    String cardNumber;

    String expiry;

    Boolean active;

    /** Показывет, активен ли шаблон. */
    public Boolean getActive() {
        return active;
    }

    /**
     * Имя держателя карты, к которой привязан шаблон. Указывается при совершении первого платежа в серии
     * рекуррентных платежей.
     * <p/>
     * Строка (максимум 26 символов).
     */
    public String getCardHolder() {
        return cardHolder;
    }

    /**
     * Маскированный номер банковской карты, к которой привязан шаблон. Указывается при совершении первого платежа в
     * серии рекуррентных платежей.
     * <p/>
     * Строка, содержащая десятичные цифры без разделителей [0­9] и маскировочный символ ‘x’​.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Дата и время регистрации шаблона рекуррентных платежей в системе Payler​.
     * <p/>
     * Строка, содержащая дату и время в формате «yyyy­MM­dd HH:mm:ss».
     */
    public String getCreated() {
        return created;
    }

    /**
     * Срок действия шаблона рекуррентных платежей.
     * <p/>
     * Строка, содержащая месяц и год в формате «MM/yy».
     */
    public String getExpiry() {
        return expiry;
    }

    /**
     * Идентификатор шаблона рекуррентных платажей.
     * <p/>
     * Строка. Соответствует переданному в запросе.
     */
    public String getRecurrentTemplateId() {
        return recurrentTemplateId;
    }
}
