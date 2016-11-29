package com.android.mobileassignment.presenter;

import android.app.Activity;

import com.android.mobileassignment.model.SubmitLocationModel;


/**
 * Presenter class to communicate between the view and model classes and perform validation.
 */
public class LocationPresenter {

    private Activity mContext;

    public LocationPresenter(Activity context) {
        mContext = context;
    }

    public void submitLocationToServer(String name,String latitude, String longitude) {
        SubmitLocationModel submitLocationModel = new SubmitLocationModel(mContext);
        submitLocationModel.submitLocation(name,latitude, longitude);
    }

}
