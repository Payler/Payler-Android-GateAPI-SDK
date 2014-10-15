package com.payler.paylergateapi;

import android.app.Fragment;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.payler.paylergateapi.lib.PaylerGateAPI;
import com.payler.paylergateapi.lib.model.ConnectionException;
import com.payler.paylergateapi.lib.model.PaylerGateException;
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
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paylerGateAPI = new PaylerGateAPI(Credentials.TEST_MERCHANT_KEY, Credentials.TEST_SERVER_URL);
    }

    public static PayFragment newInstance() {
        PayFragment fragment = new PayFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_pay, container, false);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        Log.i("", "Started");

        webView = (WebView) v.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        progressDialog = new ProgressDialog(getActivity());

        sendButton = (Button) v.findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSession();
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
//                    Toast.makeText(getActivity(), mSessionId, Toast.LENGTH_LONG).show();
                    Log.d("GATE_SESSION", sessionResponse.getSessionId());
                    startPayment();
                }
                progressBar.setVisibility(View.GONE);
            }
        }.execute();
    }

    private void startPayment() {
        paylerGateAPI.pay(mSessionId, Credentials.TEST_REDIRECT_URL, webView, false,
                new OnCompleteListener() {
            @Override
            public void onSuccess() {
//                webView.setVisibility(View.GONE);
                checkPaymentStatus();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void checkPaymentStatus() {
        // check payment status here
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage(getResources().getString(R.string.check_payment));
        progressDialog.show();

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
                    Toast.makeText(getActivity(), "Status received", Toast.LENGTH_LONG).show();
                    Log.d("GATE_SESSION", statusResponse.getStatus());
                }
                progressDialog.dismiss();
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
