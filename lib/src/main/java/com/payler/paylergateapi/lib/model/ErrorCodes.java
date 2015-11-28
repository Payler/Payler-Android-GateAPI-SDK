// Этот файл был сгенерирован автоматически.  Не меняйте его вручную.
// Версия: 1.15

package com.payler.paylergateapi.lib.model;

/** Коды ошибок возвращаемых Payler Gate API. */
public class ErrorCodes {

    /** Ошибки нет, все в порядке. */
    public static int NONE = 0;

    /** Неверно указана сумма транзакции. */
    public static int INVALID_AMOUNT = 1;

    /** Превышен баланс карты. */
    public static int BALANCE_EXCEEDED = 2;

    /** Попытка создать сессию оплаты заказа, для идентификатора которого уже была ранее зарегистрирована сессия. */
    public static int DUPLICATE_ORDER_ID = 3;

    /** Банк-эмитент карты отказал в операции. Для получения детальной информации требуется связаться с его службой поддержки. */
    public static int ISSUER_DECLINED_OPERATION = 4;

    /** Превышен лимит (достигнуты ограничительные условия по карте). */
    public static int LIMIT_EXCEEDED = 5;

    /** Транзакция отклонена, так как носит подозрительный характер. */
    public static int AF_DECLINED = 6;

    /** Операция не может быть выполнена, так как платеж находится в недопустимом состоянии. */
    public static int INVALID_ORDER_STATE = 7;

    /** Сессия платежа с указанным идентификатором не найдена. */
    public static int ORDER_NOT_FOUND = 9;

    /** Ошибка общего характера, возникшая при взаимодействии с процессинговым центром банка. Требуется связаться со службой поддержки. */
    public static int PROCESSING_ERROR = 10;

    /** Частичная разблокировка не поддерживается (сумма авторизации не может быть изменена). */
    public static int PARTIAL_RETRIEVE_NOT_ALLOWED = 11;

    /** Отказ шлюза в выполнении транзакции (возможно, по причине непредвиденной ошибки). Требуется связаться со службой поддержки. */
    public static int GATE_DECLINED = 13;

    /** Указаны некорректные данные карты. */
    public static int INVALID_CARD_INFO = 14;

    /** Указан некорректный номер карты. */
    public static int INVALID_CARDNUMBER = 15;

    /** Указано некорректное имя держателя карты. */
    public static int INVALID_CARDHOLDER = 16;

    /** Данный метод Payler API не разрешен к использованию с IP-адреса, с которого выполнен запрос. */
    public static int API_NOT_ALLOWED = 18;

    /** Доступ запрещен, так как пароль некорректен. */
    public static int INVALID_PASSWORD = 19;

    /** Один из параметров запроса имеет некорректное значение. */
    public static int INVALID_PARAMS = 20;

    /** Время, отведенное на осуществление платежа, истекло. */
    public static int SESSION_TIMEOUT = 21;

    /** Описание продавца не найдено. */
    public static int MERCHANT_NOT_FOUND = 22;

    /** Сессия платежа не найдена. */
    public static int SESSION_NOT_FOUND = 24;

    /** Срок действия карты истек. */
    public static int CARD_EXPIRED = 25;

    /** Шаблон рекуррентных платежей c указанным идентификатором не найден. */
    public static int RECURRENT_TEMPLATE_NOT_FOUND = 26;

    /** Шаблон рекуррентных платежей неактивен. */
    public static int RECURRENT_TEMPLATE_NOT_ACTIVE = 27;

    /** Ранее не было выполнено транзакций по шаблону. */
    public static int NO_TRANSACTION_BY_TEMPLATE = 28;

    /** Рекуррентные платежи не поддерживаются. Для включения данной функциональности требуется обратиться в службу поддержки. */
    public static int RECURRENT_PAYMENTS_NOT_SUPPORTED = 100;

    /** Срок действия шаблона рекуррентных платежей истек. */
    public static int EXPIRED_RECURRENT_TEMPLATE = 101;

    /** Шаблон рекуррентных платежей зарегистрирован на другой терминал. */
    public static int RECURRENT_TEMPLATE_ANOTHER_TERMINAL = 102;

    /** Не удалось обновить статус активности шаблона рекуррентных платежей. Требуется связаться со службой поддержки. */
    public static int FAILED_UPDATE_ACTIVE_STATUS = 103;

    /** Активация шаблона рекуррентных платежей требует подтверждения со стороны банка. */
    public static int TEMPLATE_ACTIVATION_REQUIRES_BANK_CONF = 104;

    /** Возвраты рекуррентных платежей не поддерживаются банком. */
    public static int REFUND_OF_RECURRENT_NOT_SUPPORTED = 105;

    /** Слишком частые рекуррентные платежи. */
    public static int TOO_FREQUENT_RECURRENT_PAYMENTS = 106;

    /** Частичный возврат не поддерживается (невозможно вернуть покупателю часть ранее списанной суммы). */
    public static int PARTIAL_REFUND_NOT_ALLOWED = 200;

    /** Последующие возвраты не поддерживаются в автоматическом режиме. Требуется обратиться в службу поддержки для их выполнения. */
    public static int MULTIPLE_REFUND_NOT_SUPPORTED = 201;

    /** Истек период изменения суммы заблокированных средств. */
    public static int EXPIRED_RETRIEVE_PERIOD = 300;

    /** Указано некорректное значение месяца истечения срока действия карты. */
    public static int INVALID_EXPIRY_MONTH = 400;

    /** Указано некорректное значение года истечения срока действия карты. */
    public static int INVALID_EXPIRY_YEAR = 401;

    /** Указано некорректное значение кода подлинности карты. */
    public static int INVALID_SECURE_CODE = 402;

    /** Карта неактивна. Необходимо оплатить другой картой или связаться с банком-эмитентом. */
    public static int CARD_INACTIVE = 500;

    /** Операция не поддерживается картой. Требуется связаться со службой поддержки банка-эмитента карты. */
    public static int OPERATION_NOT_SUPPORTED = 501;

    /** Операция отменена по желанию держателя карты. */
    public static int DECLINED_BY_CARDHOLDER = 502;

    /** Ошибка обработки PIN-кода карты. */
    public static int PIN_ERROR = 503;

    /** Ограниченная карта. */
    public static int RESTRICTED_CARD = 504;

    /** Неверный статус карты. */
    public static int INVALID_CARD_STATUS = 505;

    /** Повторная операция. */
    public static int DUPLICATED_OPERATION = 600;

    /** Запрос осуществления платежа уже обрабатывается. */
    public static int IN_PROGRESS_ERROR = 601;

    /** Заказ был оплачен ранее. */
    public static int PAID_EARLIER = 602;

    /** Не найдено зарегистрированных транзакций по указанному идентификатору заказа (пользователь не предпринимал попыток оплаты). */
    public static int DEAL_NOT_FOUND = 603;

    /** Транзакция имеет неподдерживаемый для текущей операции тип. */
    public static int INCORRECT_TRANSACTION_TYPE = 604;

    /** Невозможно выполнить операцию в рамках не двухстадийного платежа. */
    public static int TRANSACTION_NOT_TWO_STEP = 605;

    /** Не найдено описание попытки платежа с указанным идентификатором. Требуется связаться со службой поддержки. */
    public static int ATTEMPT_NOT_FOUND = 606;

    /** Превышено максимальное число попыток осуществления платежа. */
    public static int ATTEMPTS_NUMBER_EXCEEDED = 607;

    /** Начата новая попытка оплаты, продолжение старой невозможно. */
    public static int THERE_IS_NEWER_ATTEMPT = 608;

    /** Невозможно создать указанный шаблон ответа. */
    public static int TEMPLATE_NOT_FOUND = 700;

    /** Не задан URL перенаправления на сайт продавца после осуществления платежа. */
    public static int RETURN_URL_NOT_SET = 701;

    /** Терминал продавца с указанным платежным идентификатором не найден. */
    public static int TERMINAL_NOT_FOUND = 702;

    /** Данная валюта не поддерживается. */
    public static int CURRENCY_NOT_SUPPORTED = 703;

    /** 3-D Secure авторизация на стороне банка-эмитента карты отменена или ее выполнение невозможно из-за возникшей ошибки. */
    public static int THREE_DS_FAIL = 800;

    /** Не найдено описание результата попытки осуществления платежа, требующего 3-D Secure подтверждения. Требуется связаться со службой поддержки. */
    public static int NO_RESULT_OF_3DS = 801;

    /** Информация о предварительной обработке 3D Secure запроса не найдена. Требуется связаться со службой поддержки. */
    public static int PREPROCESS_3DS_INFO_NOT_FOUND = 802;

    /** Требуется карта с поддержкой технологии 3-D Secure. */
    public static int NOT_INVOLVED_IN_3DS = 803;

    /** Операция не разрешена продавцу (магазину). Требуется связаться со службой поддержки. */
    public static int OPERATION_NOT_ALLOWED_TO_MERCHANT = 900;

    /** Операция выполнена не полностью. Требуется связаться со службой поддержки. */
    public static int COMPLETED_PARTIALLY = 901;

    /** Сверка итогов завершилась с ошибкой. Требуется связаться со службой поддержки. */
    public static int RECONCILE_ERROR = 902;

    /** Операция отклонена. Требуется связаться со службой поддержки. */
    public static int DECLINED = 903;

    /** Временная проблема. Попробуйте выполнить операцию позднее. */
    public static int TEMPORARY_MALFUNCTION = 904;

    /** Возврат для платежей через электронный кошелёк не поддерживается. */
    public static int EMONEY_REFUND_NOT_SUPPORTED = 1000;

}