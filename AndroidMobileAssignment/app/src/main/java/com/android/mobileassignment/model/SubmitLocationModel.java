package com.android.mobileassignment.model;

import android.app.Activity;
import android.widget.Toast;

import com.android.mobileassignment.AppConstants;
import com.android.mobileassignment.R;
import com.android.mobileassignment.data.ErrorType;
import com.android.mobileassignment.utils.AppUtils;
import com.android.mobileassignment.utils.network.VolleyServiceGateway;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Its a model class to submit latitude and longitude to server
 */
public class SubmitLocationModel {

    private Activity mContext;
    private EventBus mEventBus = EventBus.getDefault();

    public SubmitLocationModel(Activity context) {
        mContext = context;
    }

    public void submitLocation(final String latitude, final String longitude) {

        VolleyServiceGateway volleyService = new VolleyServiceGateway(mContext) {
            @Override
            protected Map<String, String> makeHeaders() {

                HashMap<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put(AppConstants.LATITUDE, latitude);
                params.put(AppConstants.LONGITUDE, longitude);
                return params;
            }

            @Override
            protected void processResponse(String response) {

            }

            @Override
            protected void processResponseCode(int code) {
                final int statusCode = code;

                mContext.runOnUiThread(new Runnable() {
                    public void run() {
                        if (statusCode == 201) {
                            Date startTime = new Date();
                            AppUtils.putStringToPref(mContext, AppConstants.TIME, Long.toString(startTime.getTime()));
                            Toast.makeText(mContext, mContext.getString(R.string.success_submit_location), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.failure_submit_location), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            protected void processError(VolleyError errorResponse) {
                mEventBus.post(errorResponse);
            }

            @Override
            protected void processNetworkError() {
                mEventBus.post(ErrorType.NO_INTERNET);
            }
        };

        volleyService.postData(AppConstants.SUBMIT_LOCATION_URL);
    }

}
