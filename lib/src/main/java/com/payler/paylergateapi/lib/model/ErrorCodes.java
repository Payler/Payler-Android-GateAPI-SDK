// Этот файл был сгенерирован автоматически.  Не меняйте его вручную.
// Версия: 1.10

package com.payler.paylergateapi.lib.model;

/** Коды ошибок возвращаемых Payler Gate API. */
public class ErrorCodes {

    /** Ошибки нет.  Всё в порядке. */
    public static int NONE = 0;

    /** Неверно указана сумма транзакции. */
    public static int INVALID_AMOUNT = 1;

    /** Превышен бананс */
    public static int BALANCE_EXCEEDED = 2;

    /** Попытка создать сессию оплаты заказа, для идентификатора которого уже была ранее зарегистрирована сессия. */
    public static int DUPLICATE_ORDER_ID = 3;

    /** Эмитент карты отказал в операции */
    public static int ISSUER_DECLINED_OPERATION = 4;

    /** Превышен лимит */
    public static int LIMIT_EXCEEDED = 5;

    /** Транзакция отклонена АнтиФрод механизмом */
    public static int AF_DECLINED = 6;

    /** Попытка выполнения транзакции для недопустимого состояния платежа. */
    public static int INVALID_ORDER_STATE = 7;

    /** Превышен лимит магазина или транзакции запрещены Магазину. */
    public static int MERCHANT_DECLINED = 8;

    /** Платёж с указанным OrderId не найден. */
    public static int ORDER_NOT_FOUND = 9;

    /** Ошибка при взаимодействии с процессинговым центром. */
    public static int PROCESSING_ERROR = 10;

    /** Изменение суммы авторизации не может быть выполнено. */
    public static int PARTIAL_RETRIEVE_NOT_ALLOWED = 11;

    /** Возврат не может быть выполнен. */
    public static int REFUND_NOT_ALLOWED = 12;

    /** Отказ шлюза в выполнении транзакции. */
    public static int GATE_DECLINED = 13;

    /** Введены неправильные параметры карты. */
    public static int INVALID_CARD_INFO = 14;

    /** Неверный номер карты. */
    public static int INVALID_CARD_PAN = 15;

    /** Недопустимое имя держателя карты. */
    public static int INVALID_CARDHOLDER = 16;

    /** Некорректный параметр PayInfo (неправильно сформирован или нарушена крипта) */
    public static int INVALID_PAY_INFO = 17;

    /** Данный метод Payler API не разрешен к использованию с IP-адреса, с которого выполнен запрос. */
    public static int API_NOT_ALLOWED = 18;

    /** Параметры доступа некорретны (к параметрам доступа относятся ключ терминала продавца, пароль и т.п.). */
    public static int ACCESS_DENIED = 19;

    /** Неверный набор или формат параметров. */
    public static int INVALID_PARAMS = 20;

    /** Время платежа истекло. */
    public static int SESSION_TIMEOUT = 21;

    /** Описание продавца не найдено. */
    public static int MERCHANT_NOT_FOUND = 22;

    /** Непредвиденная ошибка. */
    public static int UNEXPECTED_ERROR = 23;

    /** Сессия платежа не найдена. */
    public static int SESSION_NOT_FOUND = 24;

    /** Карта просрочена */
    public static int CARD_EXPIRED = 25;

    /** Шаблон рекуррентных платежей не найден. */
    public static int RECURRENT_TEMPLATE_NOT_FOUND = 26;

    /** Шаблон рекуррентных платежей неактивен. */
    public static int RECURRENT_TEMPLATE_NOT_ACTIVE = 27;

    /** Ранее не было выполнено транзакций по шаблону. */
    public static int NO_TRANSACTION_BY_TEMPLATE = 28;

}
