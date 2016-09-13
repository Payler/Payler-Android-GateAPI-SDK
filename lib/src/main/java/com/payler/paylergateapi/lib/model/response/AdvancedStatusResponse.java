package com.payler.paylergateapi.lib.model.response;

import java.util.HashMap;

public class AdvancedStatusResponse extends Response {
    String orderId;

    Long amount;

    String status;

    String reccurentTemplateId;

    String cardNumber;

    String cardHolder;

    String dt;

    String from;

    String approvalCode;

    String rrn;

    String userdata;

    UserEnteredParams userEnteredParams;

    String type;

    String processing;

    String processing_order_id;

    String cardBankname;

    String cardPaymentssystem;

    String cardProduct;

    /**
     * Маскированный номер карты, с помощью которой осуществлен  платеж.
     * <p/>
     * Необязателен.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Имя держателя карты , с помощью которой осуществлен платеж.
     * <p/>
     * Необязателен. Максимум 26 символов.
     */
    public String getCardHolder() {
        return cardHolder;
    }

    /**
     * Сумма платежа в копейках.
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Буквенно­цифровой код, присвоенный банком­эмитентом для удостоверения подтверждения запроса авторизации.
     * <p/>
     * Необязателен. Строка (6 символов).
     */
    public String getApprovalCode() {
        return approvalCode;
    }

    /**
     * Название банка.
     * <p/>
     * Строка. Может быть пустым значением.
     */
    public String getCardBankName() {
        return cardBankname;
    }

    /**
     * Платежная система.
     * <p/>
     * Строка. Может быть пустым значением.
     */
    public String getCardPaymentsSystem() {
        return cardPaymentssystem;
    }

    /**
     * Тип карты.
     * <p/>
     * Строка. Может быть пустым значением.
     */
    public String getCardProduct() {
        return cardProduct;
    }

    /**
     * Время регистрации транзакции в системе Payler​.
     * <p/>
     * Строка, содержащая дату и время в формате «yyyy­MM­dd HH:mm:ss».
     */
    public String getTransactionDate() {
        return dt;
    }

    /**
     * IP­адрес, с которого выполнен запрос осуществления платежа.
     * <p/>
     * Необязателен. Строка.
     */
    public String getIpAddress() {
        return from;
    }

    /**
     * Идентификатор платежа в системе Продавца.
     * Строка. Соответствует переданному в запросе.
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Название процессинга, обрабатывающего данный платёж.
     * <p/>
     * Необязателен. Строка.
     */
    public String getProcessing() {
        return processing;
    }

    /**
     * Идентификатор платежа в процессинговом центре.
     * <p/>
     * Необязателен. Строка.
     */
    public String getProcessingOrderId() {
        return processing_order_id;
    }

    /**
     * Идентификатор шаблона рекуррентных платежей. Присутствует, если в рамках текущей транзакции был создан шаблон
     * рекуррентных платежей или она была осуществлена по шаблону.
     * <p/>
     * Необязателен. Строка (максимум 100 символов).
     */
    public String getReccurentTemplateId() {
        return reccurentTemplateId;
    }

    /**
     * Номер, присвоенный транзакции в платежной системе.
     * <p/>
     * Необязателен. Строка.
     */
    public String getTransactionNumber() {
        return rrn;
    }

    /**
     * Состояние платежа.
     * <p/>
     * см. статусы транзакций.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Определяет количество стадий платежа.
     * <p/>
     * Строка. “OneStep” ​– одностадийный платеж;  “TwoStep”​– двухстадийный
     * платеж.
     */
    public String getType() {
        return type;
    }

    /**
     * Значение параметра userdata,​​переданного в методе StartSession
     * <p/>
     * Необязателен. Строка.
     */
    public String getUserdata() {
        return userdata;
    }


    /**
     * В форме на странице оплаты можно указать дополнительные поля для ввода,  начинающиеся с user_entered_.​
     * <p/>
     * Максимальная длина каждого параметра ­ 100 символов. Затем все эти поля возвращаются в GetAdvancedStatus в
     * поле user_entered_params
     */
    public HashMap<String, String> getUserEnteredParams() {
        return userEnteredParams;
    }

    public static class UserEnteredParams extends HashMap<String, String> {

    }
}
