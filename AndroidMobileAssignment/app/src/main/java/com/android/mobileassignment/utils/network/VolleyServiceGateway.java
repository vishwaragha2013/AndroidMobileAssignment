package com.android.mobileassignment.utils.network;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.android.mobileassignment.AppConstants;
import com.android.mobileassignment.utils.AppUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a Volley Service gateway for all Network calls
 */
public abstract class VolleyServiceGateway {

    private static final int MAX_TRIES = 0;


    private Context mContext;

    public VolleyServiceGateway(Context context) {
        mContext = context;
    }

    abstract protected Map<String, String> makeHeaders();

    abstract protected Map<String, String> getParamsData();

    abstract protected void processResponse(String response);

    abstract protected void processResponseCode(int statusCode);

    abstract protected void processError(VolleyError errorResponse);

    abstract protected void processNetworkError();


    private void connectStringRequest(String url, int type) {

        //Check if Internet connectivity is available or not.
        // Send out a error if its not available
        if (!AppUtils.isNetworkAvailable(mContext)) {
            processNetworkError();
            return;
        }

        RequestQueue queue = VolleySingleton.getInstance(mContext)
                .getRequestQueue();
        StringRequest stringRequest = new StringRequest(type, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        processError(error);
                    }
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.w("TAG", "Response " + response.statusCode);
                processResponseCode(response.statusCode);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> paramsData=getParamsData();
                return paramsData;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = makeHeaders();
                String credentials = AppConstants.UN + ":" + AppConstants.PWD;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put(AppConstants.AUTHORIZATION_KEY, auth);
                return headers;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                MAX_TRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }


    /**
     * Make Http Post request.
     *
     * @param url
     */
    public void postData(String url) {
        connectStringRequest(url, Request.Method.POST);
    }

}

