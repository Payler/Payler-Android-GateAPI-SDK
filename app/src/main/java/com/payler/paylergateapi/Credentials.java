package com.payler.paylergateapi;

public class Credentials {

    /**
     * Адрес сервера Пэйлер.
     * Возможные варианты:
     *  - https://sandbox.payler.com - тестовая среда
     *  - https://secure.payler.com - продакшен среда
     */
    public static final String TEST_SERVER_URL = "https://sandbox.payler.com";

    /**
     * Идентификатор продавца.
     * Обратитесь, пожалуйста, в службу поддержки 24@payler.com для получения ключа.
     */
    public static final String TEST_MERCHANT_KEY = "YOUR_PAYMENT_KEY";

    /**
     * Адрес страницы на сайте продавца, на которую происходит редирект после выполнения оплаты.
     */
    public static final String TEST_REDIRECT_URL = "http://localhost:7820";

    /**
     * Платёжный пароль.
     * Нужен для операций возврата.
     */
    public static final String TEST_MERCHANT_PASSWORD = "123";
}
