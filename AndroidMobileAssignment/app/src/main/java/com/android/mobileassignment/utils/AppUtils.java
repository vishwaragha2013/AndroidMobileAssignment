package com.android.mobileassignment.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;

import com.android.mobileassignment.R;

/**
 * All the utility methods which are used in the App can be placed here
 */
public class AppUtils {


    private static ProgressDialog sProgressDialog;
    private static final String APP_STATE = "com.android.mobileassignment";


    /**
     * Shows Progress Dialog with only Spinner in the center
     *
     * @param context parameter to get context for performing related task
     */
    public static void showProgressDialog(Context context) {
        sProgressDialog = new ProgressDialog(context);
        sProgressDialog.setCancelable(true);
        sProgressDialog.setCanceledOnTouchOutside(true);
        sProgressDialog = ProgressDialog.show(context, null, null);
        sProgressDialog.setContentView(new ProgressBar(context));
        sProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    /**
     * Hides the Progress Dialog if its showing
     */
    public static void hideProgressDialog() {
        if ((sProgressDialog != null) && sProgressDialog.isShowing()) {
            sProgressDialog.cancel();
            sProgressDialog = null;
        }
    }


    /**
     * Checks if a device is connected to internet or not
     *
     * @param context application context
     * @return true - if the device connected to internet; false - otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static String getErrorMessage(Context context, int errorCode) {
        String errorMsg;

        switch (errorCode) {
            default:
                errorMsg = "Network Error";
                break;
        }
        return errorMsg;
    }

    /**
     * Shows Dialog with OK button
     *
     * @param activityContext application context
     * @param messageId       id of the message to be shown
     */
    public static void showDialog(final Activity activityContext, int messageId) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activityContext);
        alertDialogBuilder.setMessage(activityContext.getResources().getString(messageId));
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }


    /**
     * method to add string to shared preference
     *
     * @param context     application context
     * @param key         key to saved inside sharedPreference
     * @param stringValue value to be assigned the key
     */
    public static void putStringToPref(Context context, String key,
                                       String stringValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                APP_STATE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, stringValue);
        editor.apply();
    }

    /**
     * method returns the string value from shared preferences
     *
     * @param context    application context
     * @param key        key to saved inside sharedPreference
     * @param defaultVal default value inside sharedPreference
     * @return stringValue
     */
    public static String getStringFromPref(Context context, String key,
                                           String defaultVal) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                APP_STATE, Context.MODE_PRIVATE);

        return sharedPref.getString(key, defaultVal);
    }


}
