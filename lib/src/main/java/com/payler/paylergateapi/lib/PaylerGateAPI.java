package com.payler.paylergateapi.lib;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.payler.paylergateapi.lib.model.response.MoneyResponse;
import com.payler.paylergateapi.lib.model.response.RetrieveResponse;
import com.payler.paylergateapi.lib.model.response.SessionResponse;
import com.payler.paylergateapi.lib.model.response.StatusResponse;

public class PaylerGateAPI {

    enum SessionType {
        ONE_STEP,
        TWO_STEP
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

    public PaylerGateAPI(String merchantKey, String serverUrl) {
        mMerchantKey = merchantKey;
        mServerUrl = serverUrl;
    }

    public SessionResponse startSession(SessionType type, String orderId, long amount, String product,
                             float total, String template, String lang, boolean recurrent) {
        return null;
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

    private void executeRequest()

    class PayWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}
