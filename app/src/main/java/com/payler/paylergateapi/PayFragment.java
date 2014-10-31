package com.payler.paylergateapi;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.payler.paylergateapi.lib.PaylerGateAPI;
import com.payler.paylergateapi.lib.model.ConnectionException;
import com.payler.paylergateapi.lib.model.PaylerGateException;
import com.payler.paylergateapi.lib.model.TransactionStatus;
import com.payler.paylergateapi.lib.model.response.MoneyResponse;
import com.payler.paylergateapi.lib.model.response.SessionResponse;
import com.payler.paylergateapi.lib.model.response.StatusResponse;
import com.payler.paylergateapi.lib.utils.OnCompleteListener;

public class PayFragment extends Fragment {

    private PaylerGateAPI paylerGateAPI;
    private String mSessionId;
    private String mOrderId;

    private OnFragmentInteractionListener mListener;
    private Button sendButton;
    private ProgressBar progressBar;
    private WebView webView;
    private TextView statusText;

    private boolean mPaymentCharged = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paylerGateAPI = new PaylerGateAPI(Credentials.TEST_MERCHANT_KEY, Credentials.TEST_SERVER_URL);
    }

    public static PayFragment newInstance() {
        PayFragment fragment = new PayFragment();
        return fragment;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_pay, container, false);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        Log.i("", "Started");

        webView = (WebView) v.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        statusText = (TextView) v.findViewById(R.id.status_text);

        sendButton = (Button) v.findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPaymentCharged) {
                    returnMoney();
                } else {
                    startSession();
                }
            }
        });

        return v;
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
                            1000, null, 0f, null, null, false);
                } catch (final PaylerGateException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("API", String.valueOf(e.getCode()) + ": " + e.getMessage());
                        }
                    });
                } catch (final ConnectionException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final PaylerGateException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final PaylerGateException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
