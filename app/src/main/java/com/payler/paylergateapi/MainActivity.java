package com.payler.paylergateapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.payler.paylergateapi.lib.PaylerGateAPI;
import com.payler.paylergateapi.lib.model.ConnectionException;
import com.payler.paylergateapi.lib.model.PaylerGateException;
import com.payler.paylergateapi.lib.model.TransactionStatus;
import com.payler.paylergateapi.lib.model.response.GetTemplateResponse;
import com.payler.paylergateapi.lib.model.response.MoneyResponse;
import com.payler.paylergateapi.lib.model.response.SessionResponse;
import com.payler.paylergateapi.lib.model.response.StatusResponse;
import com.payler.paylergateapi.lib.utils.OnCompleteListener;


public class MainActivity extends Activity {

    private PaylerGateAPI paylerGateAPI;
    private String mSessionId;
    private String mOrderId;
    private String mRecurrentTemplateId;

    private Button sendButton;
    private Button sendRecurrentButton;
    private ProgressBar progressBar;
    private WebView webView;
    private TextView statusText;

    private boolean mIsRecurrent = false;
    private boolean mPaymentCharged = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        paylerGateAPI = new PaylerGateAPI(Credentials.TEST_MERCHANT_KEY, Credentials.TEST_SERVER_URL);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        Log.i("", "Started");

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        statusText = (TextView) findViewById(R.id.status_text);

        sendButton = (Button) findViewById(R.id.send);
        sendRecurrentButton = (Button) findViewById(R.id.recurrent);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPaymentCharged) {
                    if (view.getId() == R.id.send) {
                        returnMoney();
                    } else {
                        if (mIsRecurrent) {
                            repeatPay();
                        }
                    }
                } else {
                    mIsRecurrent = (view.getId() == R.id.recurrent);
                    startSession();
                }
            }
        };
        sendButton.setOnClickListener(listener);
        sendRecurrentButton.setOnClickListener(listener);
    }

    private String getMockOrderId() {
        mOrderId = "oid" + String.valueOf(System.currentTimeMillis());
        return mOrderId;
    }

    private void startSession() {
        progressBar.setVisibility(View.VISIBLE);
        statusText.setText(R.string.load_webform);
        new AsyncTask<Void, Void, SessionResponse>() {
            @Override
            protected SessionResponse doInBackground(Void... voids) {
                try {
                    return paylerGateAPI.startSession(PaylerGateAPI.SessionType.ONE_STEP, getMockOrderId(),
                            1000, null, 0f, null, null, mIsRecurrent);
                } catch (final PaylerGateException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("API", String.valueOf(e.getCode()) + ": " + e.getMessage());
                        }
                    });
                } catch (final ConnectionException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(SessionResponse sessionResponse) {
                super.onPostExecute(sessionResponse);
                if (sessionResponse != null) {
                    mSessionId = sessionResponse.getSessionId();
                    Log.d("GATE_SESSION", sessionResponse.getSessionId());
                    startPayment();
                }
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void startPayment() {
        paylerGateAPI.pay(mSessionId, Credentials.TEST_REDIRECT_URL, webView, false,
                new OnCompleteListener() {
                    @Override
                    public void onSuccess() {
                        webView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        checkPaymentStatus();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    private void checkPaymentStatus() {

        statusText.setText(R.string.check_payment);
        statusText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, StatusResponse>() {

            @Override
            protected StatusResponse doInBackground(Void... voids) {

                try {
                    return paylerGateAPI.getStatus(mOrderId);
                } catch (final ConnectionException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final PaylerGateException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("API", String.valueOf(e.getCode()) + ": " + e.getMessage());
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(StatusResponse statusResponse) {
                super.onPostExecute(statusResponse);
                if (statusResponse != null) {
                    if (statusResponse.getStatus().equals(TransactionStatus.CHARGED)) {
                        if (mIsRecurrent) {
                            mRecurrentTemplateId = statusResponse.getRecurrentTemplateId();
                            getTemplateInfo();
                            sendRecurrentButton.setText(R.string.repeat);
                        }
                        statusText.setText(R.string.payment_ok);
                        mPaymentCharged = true;
                        sendButton.setText(R.string.refund_text);
                    } else {
                        statusText.setText(R.string.payment_error);
                    }
                    Log.d("GATE_SESSION", statusResponse.getStatus());
                } else {
                    statusText.setText(R.string.status_not_received);
                }
                progressBar.setVisibility(View.INVISIBLE);
                sendRecurrentButton.setEnabled(true);
            }
        }.execute();

    }

    private String templateInfoToString(GetTemplateResponse data) {
        return "ID: " +
                data.getRecurrentTemplateId() +
                "\n" +
                "Создан: " +
                data.getCreated() +
                "\n" +
                "Владелец карты: " +
                data.getCardHolder() +
                "\n" +
                "Номер карты: " +
                data.getCardNumber() +
                "\n" +
                "Срок действия карты: " +
                data.getExpiry() +
                "\n" +
                "Активен: " +
                (data.isActive() ? "Да" : "Нет");
    }

    private void getTemplateInfo() {
        statusText.setText(R.string.status_template_info);
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, GetTemplateResponse>() {

            @Override
            protected GetTemplateResponse doInBackground(Void... params) {
                try {
                    return paylerGateAPI.getTemplate(mRecurrentTemplateId);
                } catch (final ConnectionException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final PaylerGateException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("API", String.valueOf(e.getCode()) + ": " + e.getMessage());
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(GetTemplateResponse getTemplateResponse) {
                super.onPostExecute(getTemplateResponse);
                if (getTemplateResponse != null) {
                    statusText.setText(templateInfoToString(getTemplateResponse));
                } else {
                    statusText.setText(R.string.get_template_error);
                }
                progressBar.setVisibility(View.INVISIBLE);
                sendRecurrentButton.setEnabled(true);
                sendRecurrentButton.setText(R.string.repeat);
            }
        }.execute();
    }

    private void repeatPay() {
        statusText.setText(R.string.status_repeat_pay);
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, MoneyResponse>() {

            @Override
            protected MoneyResponse doInBackground(Void... params) {
                try {
                    return paylerGateAPI.repeatPay(getMockOrderId(), mRecurrentTemplateId, 100);
                } catch (final ConnectionException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final PaylerGateException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("API", String.valueOf(e.getCode()) + ": " + e.getMessage());
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(MoneyResponse moneyResponse) {
                super.onPostExecute(moneyResponse);
                if (moneyResponse != null) {
                    float amount = moneyResponse.getAmount() / 100f;
                    statusText.setText(String.format(getString(R.string.paid_sum), amount));
                } else {
                    statusText.setText(R.string.payment_error);
                }
                mPaymentCharged = false;
                sendButton.setText(R.string.onestep_payment);
                sendRecurrentButton.setText(R.string.recurrent_payment);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    private void returnMoney() {
        statusText.setText(R.string.refund_progress);
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, MoneyResponse>() {

            @Override
            protected MoneyResponse doInBackground(Void... params) {
                try {
                    return paylerGateAPI.refund(Credentials.TEST_MERCHANT_PASSWORD, mOrderId, 100);
                } catch (final ConnectionException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final PaylerGateException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("API", String.valueOf(e.getCode()) + ": " + e.getMessage());
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(MoneyResponse moneyResponse) {
                super.onPostExecute(moneyResponse);
                if (moneyResponse != null) {
                    float amount = moneyResponse.getAmount() / 100f;
                    statusText.setText(String.format(getString(R.string.refunded_sum), amount));
                } else {
                    statusText.setText(R.string.refund_error);
                }
                mPaymentCharged = false;
                sendButton.setText(R.string.onestep_payment);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

}
