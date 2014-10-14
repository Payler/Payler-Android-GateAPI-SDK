package com.payler.paylergateapi;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.payler.paylergateapi.lib.PaylerGateAPI;
import com.payler.paylergateapi.lib.model.ConnectionException;
import com.payler.paylergateapi.lib.model.PaylerGateException;
import com.payler.paylergateapi.lib.model.response.SessionResponse;

public class PayFragment extends Fragment {

    private PaylerGateAPI paylerGateAPI;

    private OnFragmentInteractionListener mListener;
    private Button sendButton;
    private ProgressBar progressBar;

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
        return "oid" + String.valueOf(System.currentTimeMillis());
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
                    Toast.makeText(getActivity(), sessionResponse.getSessionId(), Toast.LENGTH_LONG).show();
                    Log.d("GATE_SESSION", sessionResponse.getSessionId());
                }
                progressBar.setVisibility(View.GONE);
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
