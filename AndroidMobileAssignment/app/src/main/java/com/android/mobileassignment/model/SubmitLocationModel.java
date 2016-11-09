package com.android.mobileassignment.model;

import android.content.Context;
import com.android.mobileassignment.AppConstants;
import com.android.mobileassignment.data.ErrorType;
import com.android.mobileassignment.utils.network.VolleyServiceGateway;
import com.android.volley.VolleyError;
import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import java.util.Map;

/**
 * Its a model class to submit latitude and longitude to server
 */
public class SubmitLocationModel {

    private Context mContext;
    private EventBus mEventBus = EventBus.getDefault();

    public SubmitLocationModel(Context context) {
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
                params.put("LATITUDE", latitude);
                params.put("LONGITUDE", longitude);
                return params;
            }

            @Override
            protected void processResponse(String response) {

            }

            @Override
            protected void processResponseCode(int statusCode) {
                mEventBus.post(new Integer(statusCode));
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
