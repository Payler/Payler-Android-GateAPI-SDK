package com.payler.paylergateapi.lib;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.payler.paylergateapi.lib.model.ConnectionException;
import com.payler.paylergateapi.lib.model.PaylerGateException;
import com.payler.paylergateapi.lib.model.request.SessionRequest;
import com.payler.paylergateapi.lib.model.response.GateError;
import com.payler.paylergateapi.lib.model.response.MoneyResponse;
import com.payler.paylergateapi.lib.model.response.Response;
import com.payler.paylergateapi.lib.model.response.RetrieveResponse;
import com.payler.paylergateapi.lib.model.response.SessionResponse;
import com.payler.paylergateapi.lib.model.response.StatusResponse;
import com.payler.paylergateapi.lib.network.RequestExecutor;

public class PaylerGateAPI {

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

    public static final String LANG_EN = "en";
    public static final String LANG_RU = "ru";

    private static final String START_SESSION_URL = "/gapi/StartSession";
    private static final String PAY_URL = "/gapi/Pay";
    private static final String CHARGE_URL = "/gapi/Charge";
    private static final String RETRIEVE_URL = "/gapi/Retrieve";
    private static final String REFUND_URL = "/gapi/Refund";
    private static final String GET_STATUS_URL = "/gapi/GetStatus";

    private String mMerchantKey;
    private String mServerUrl;
    private RequestExecutor mExecutor;

    public PaylerGateAPI(String merchantKey, String serverUrl) {
        mMerchantKey = merchantKey;
        mServerUrl = serverUrl;
        mExecutor = new RequestExecutor();
    }

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

    public StatusResponse getStatus(String orderId) {
        return null;
    }

    public MoneyResponse charge(String password, String orderId, long amount) {
        return null;
    }

    public RetrieveResponse retrieve(String password, String orderId, long amount) {
        return null;
    }

    public MoneyResponse refund(String password, String orderId, long amount) {
        return null;
    }

    public void pay(String sessionId, String redirectUrl, WebView webView) {

    }

    class PayWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}
