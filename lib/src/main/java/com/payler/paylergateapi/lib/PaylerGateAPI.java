package com.payler.paylergateapi.lib;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.common.base.Strings;
import com.payler.paylergateapi.lib.model.ConnectionException;
import com.payler.paylergateapi.lib.model.ErrorCodes;
import com.payler.paylergateapi.lib.model.PaylerGateException;
import com.payler.paylergateapi.lib.model.request.ChargeRequest;
import com.payler.paylergateapi.lib.model.request.FindSessionRequest;
import com.payler.paylergateapi.lib.model.request.GetTemplateRequest;
import com.payler.paylergateapi.lib.model.request.RefundRequest;
import com.payler.paylergateapi.lib.model.request.RepeatPayRequest;
import com.payler.paylergateapi.lib.model.request.RetrieveRequest;
import com.payler.paylergateapi.lib.model.request.SessionRequest;
import com.payler.paylergateapi.lib.model.request.StatusRequest;
import com.payler.paylergateapi.lib.model.response.FindSessionResponse;
import com.payler.paylergateapi.lib.model.response.GateError;
import com.payler.paylergateapi.lib.model.response.GetTemplateResponse;
import com.payler.paylergateapi.lib.model.response.MoneyResponse;
import com.payler.paylergateapi.lib.model.response.Response;
import com.payler.paylergateapi.lib.model.response.RetrieveResponse;
import com.payler.paylergateapi.lib.model.response.SessionResponse;
import com.payler.paylergateapi.lib.model.response.StatusResponse;
import com.payler.paylergateapi.lib.network.RequestExecutor;
import com.payler.paylergateapi.lib.utils.OnCompleteListener;

import org.apache.http.util.EncodingUtils;

/**
 * Этот класс предоставляет доступ к Payler Gate API.
 * Методы класса соответсвуют методам API.
 */
public class PaylerGateAPI {

    public static final String LANG_EN = "en";

    public static final String LANG_RU = "ru";

    private static final String START_SESSION_URL = "/gapi/StartSession";

    private static final String FIND_SESSION_URL = "/gapi/FindSession";

    private static final String PAY_URL = "/gapi/Pay";

    private static final String CHARGE_URL = "/gapi/Charge";

    private static final String RETRIEVE_URL = "/gapi/Retrieve";

    private static final String REFUND_URL = "/gapi/Refund";

    private static final String GET_STATUS_URL = "/gapi/GetStatus";

    private static final String GET_TEMPLATE_URL = "/gapi/GetTemplate";

    private static final String REPEAT_PAY_URL = "/gapi/RepeatPay";

    private String mMerchantKey;

    private String mServerUrl;

    private RequestExecutor mExecutor;

    /**
     * Конструктор
     *
     * @param merchantKey Идентификатор продавца.
     * @param serverUrl   Адрес платёжного шлюза (разные значения для тестовой и продакшен среды).
     */
    public PaylerGateAPI(String merchantKey, String serverUrl) {
        mMerchantKey = merchantKey;
        mServerUrl = serverUrl;
        mExecutor = new RequestExecutor();
    }

    /**
     * Запрос инициализации платежа. Выполняется перед перенаправлением Пользователя на
     * страницу платежного шлюза Payler.
     *
     * @param type      Тип сессии. Определяет количество стадий платежа.
     * @param orderId   Идентификатор оплачиваемого заказа в системе Продавца. Для каждого платежа (сессии) требуется
     *                  использовать уникальный идентификатор.
     * @param amount    Сумма платежа в копейках.
     * @param product   Наименование оплачиваемого продукта.
     * @param total     Количество оплачиваемых в заказе продуктов.
     * @param template  Шаблон страницы оплаты. При отсутствии используется шаблон по умолчанию.
     * @param lang      Указывает предпочитаемый язык ответов на запросы.
     * @param recurrent Показывает, требуется ли создать шаблон рекуррентных платежей на основе текущего.
     * @return Ответ на успешный запрос.
     * @throws PaylerGateException
     * @throws ConnectionException
     */
    public SessionResponse startSession(SessionType type, String orderId, long amount, String product,
                                        float total, String template, String lang, boolean recurrent)
            throws PaylerGateException, ConnectionException {
        SessionRequest request = new SessionRequest();
        request.setKey(mMerchantKey)
                .setType(type.getValue())
                .setOrderId(orderId)
                .setAmount(amount)
                .setProduct(product)
                .setTotal(total)
                .setTemplate(template)
                .setLang(lang)
                .setRecurrent(recurrent);
        Response response = mExecutor.executeRequest(mServerUrl + START_SESSION_URL, request,
                                                     SessionResponse.class);
        if (response instanceof GateError) {
            GateError gateError = (GateError) response;
            throw new PaylerGateException(gateError.getCode(), gateError.getMessage());
        }
        return (SessionResponse) response;
    }

    /**
     * Поиск платёжной сессии по идентификатору платежа (order_id).
     *
     * @param orderId Идентификатор оплачиваемого заказа в системе Продавца.
     * @return Ответ на успешный запрос
     * @throws ConnectionException
     * @throws PaylerGateException
     */
    public FindSessionResponse findSession(final String orderId)
            throws ConnectionException, PaylerGateException {
        FindSessionRequest request = new FindSessionRequest();
        request
                .setKey(mMerchantKey)
                .setOrderId(orderId);
        Response response = mExecutor.executeRequest(mServerUrl + FIND_SESSION_URL,
                                                     request,
                                                     FindSessionResponse.class);
        if (response instanceof GateError) {
            GateError gateError = (GateError) response;
            throw new PaylerGateException(gateError.getCode(), gateError.getMessage());
        }
        return (FindSessionResponse) response;
    }

    /**
     * Получить актуальный статус платежа.
     *
     * @param orderId Идентификатор заказа в системе Продавца.
     * @return Параметры платежа в случае успешной обработки запроса.
     * @throws ConnectionException
     * @throws PaylerGateException
     */
    public StatusResponse getStatus(String orderId) throws ConnectionException, PaylerGateException {
        StatusRequest request = new StatusRequest();
        request.setKey(mMerchantKey)
                .setOrderId(orderId);
        Response response = mExecutor.executeRequest(mServerUrl + GET_STATUS_URL, request,
                                                     StatusResponse.class);
        if (response instanceof GateError) {
            GateError gateError = (GateError) response;
            throw new PaylerGateException(gateError.getCode(), gateError.getMessage());
        }
        return (StatusResponse) response;
    }

    /**
     * Запрос на списание средств при двухстадийной схеме проведения платежа.
     *
     * @param password Платёжный пароль продаца.
     * @param orderId  Идентификатор заказа в системе Продавца.
     * @param amount   Сумма платежа в копейках.
     * @return Результаты успешного запроса.
     * @throws ConnectionException
     * @throws PaylerGateException
     */
    public MoneyResponse charge(String password, String orderId, long amount)
            throws ConnectionException, PaylerGateException {
        ChargeRequest request = new ChargeRequest();
        request.setKey(mMerchantKey)
                .setPassword(password)
                .setOrderId(orderId)
                .setAmount(amount);
        Response response = mExecutor.executeRequest(mServerUrl + CHARGE_URL, request,
                                                     MoneyResponse.class);
        if (response instanceof GateError) {
            GateError gateError = (GateError) response;
            throw new PaylerGateException(gateError.getCode(), gateError.getMessage());
        }
        return (MoneyResponse) response;
    }

    /**
     * Выполнить частичную или полную разблокировку средств.
     *
     * @param password Платёжный пароль продаца.
     * @param orderId  Идентификатор заказа в системе Продавца.
     * @param amount   Сумма для возврата в копейках.
     * @return Результаты успешного запроса.
     * @throws ConnectionException
     * @throws PaylerGateException
     */
    public RetrieveResponse retrieve(String password, String orderId, long amount)
            throws ConnectionException, PaylerGateException {
        RetrieveRequest request = new RetrieveRequest();
        request.setKey(mMerchantKey)
                .setPassword(password)
                .setOrderId(orderId)
                .setAmount(amount);
        Response response = mExecutor.executeRequest(mServerUrl + RETRIEVE_URL, request,
                                                     RetrieveResponse.class);
        if (response instanceof GateError) {
            GateError gateError = (GateError) response;
            throw new PaylerGateException(gateError.getCode(), gateError.getMessage());
        }
        return (RetrieveResponse) response;
    }

    /**
     * Выполнить частичный или полный возврат средств покупателю.
     *
     * @param password Платёжный пароль продаца.
     * @param orderId  Идентификатор заказа в системе Продавца.
     * @param amount   Сумма для возврата в копейках.
     * @return Результаты успешного запроса.
     * @throws ConnectionException
     * @throws PaylerGateException
     */
    public MoneyResponse refund(String password, String orderId, long amount)
            throws ConnectionException, PaylerGateException {
        RefundRequest request = new RefundRequest();
        request.setKey(mMerchantKey)
                .setPassword(password)
                .setOrderId(orderId)
                .setAmount(amount);
        Response response = mExecutor.executeRequest(mServerUrl + REFUND_URL, request,
                                                     MoneyResponse.class);
        if (response instanceof GateError) {
            GateError gateError = (GateError) response;
            throw new PaylerGateException(gateError.getCode(), gateError.getMessage());
        }
        return (MoneyResponse) response;
    }

    /**
     * Перенаправление пользователя на страницу ввода карточных данных с дальнейшей блокировкой или
     * списание денежных средств.
     *
     * @param sessionId        Идентификатор платежа в системе Пэйлер.
     * @param redirectUrl      Адрес сайта магазина, на который будет сделан редирект после успешного выполнения
     *                         платежа.
     * @param webView          Ссылка на веб-вью в котором будет отображаться страница ввода карточных данных.
     * @param showRedirectPage Следует ли показывать страницу магазина, на которую делается переход.
     * @param listener         Обработчик, который будет вызван по завершению процедуры оплаты. После завершения
     *                         оплаты нужно обязательно проверить статус платежа.
     */
    public void pay(String sessionId, final String redirectUrl, WebView webView,
                    final boolean showRedirectPage, final OnCompleteListener listener) {
        byte post[] = EncodingUtils.getBytes("session_id=" + sessionId, "BASE64");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(redirectUrl)) {
                    listener.onSuccess();
                    return !showRedirectPage;
                }
                return false;
            }
        });
        webView.postUrl(mServerUrl + PAY_URL, post);
    }

    /**
     * Получение информации о шаблоне рекуррентных платежей
     *
     * @param recurrentTemplateId Идентификатор шаблона рекуррентных платежей
     * @return Результаты успешного запроса
     * @throws ConnectionException
     * @throws PaylerGateException
     */
    public GetTemplateResponse getTemplate(String recurrentTemplateId) throws ConnectionException, PaylerGateException {
        if (Strings.isNullOrEmpty(recurrentTemplateId)) {
            throw new PaylerGateException(ErrorCodes.RECURRENT_TEMPLATE_NOT_FOUND, "Не указан идентификатор шаблона " +
                    "рекуррентного платежа");
        }
        GetTemplateRequest request = new GetTemplateRequest();
        request.setKey(mMerchantKey)
                .setRecurrentTemplateId(recurrentTemplateId);
        Response response = mExecutor.executeRequest(mServerUrl + GET_TEMPLATE_URL, request,
                                                     GetTemplateResponse.class);
        if (response instanceof GateError) {
            GateError gateError = (GateError) response;
            throw new PaylerGateException(gateError.getCode(), gateError.getMessage());
        }
        return (GetTemplateResponse) response;
    }

    /**
     * @param orderId             Идентификатор заказа в системе Продавца
     * @param recurrentTemplateId Идентификатор шаблона рекуррентных платежей
     * @param amount              Сумма платежа в копейках
     * @return Результаты успешного запроса
     * @throws ConnectionException
     * @throws PaylerGateException
     */
    public MoneyResponse repeatPay(String orderId, String recurrentTemplateId, long amount)
            throws ConnectionException, PaylerGateException {
        RepeatPayRequest request = new RepeatPayRequest();
        request.setKey(mMerchantKey)
                .setOrderId(orderId)
                .setRecurrentTemplateId(recurrentTemplateId)
                .setAmount(amount);
        Response response = mExecutor.executeRequest(mServerUrl + REPEAT_PAY_URL, request,
                                                     MoneyResponse.class);
        if (response instanceof GateError) {
            GateError gateError = (GateError) response;
            throw new PaylerGateException(gateError.getCode(), gateError.getMessage());
        }
        return (MoneyResponse) response;
    }

    /**
     * Тип сессии - определяет количество стадий платежа.
     * <p/>
     * При выполнении одностадийного платежа происходит моментальное списание средств с
     * карты покупателя.  При двухстадийном платеже сначала выполняется резервирование средств, а
     * позже отдельной операцией их списание.
     */
    public enum SessionType {
        ONE_STEP(1),
        TWO_STEP(2);

        private final int value;

        SessionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
