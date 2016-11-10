package com.android.mobileassignment.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mobileassignment.R;
import com.android.mobileassignment.data.ErrorType;
import com.android.mobileassignment.presenter.LocationPresenter;
import com.android.mobileassignment.service.AppLocationService;
import com.android.mobileassignment.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity {


    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.edt_name)
    EditText edtName;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.tv_location_value)
    TextView tvLocationValue;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.tv_last_submitted)
    TextView tvLastSubmitted;

    private EventBus mEventBus = EventBus.getDefault();
    private LocationPresenter mLocationPresenter;
    private AppLocationService mGps;
    private double mLongitude;
    private double mLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mEventBus.register(this);

        tvName.setPaintFlags(tvName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLocation.setPaintFlags(tvLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mLocationPresenter = new LocationPresenter(this);

        initGps();

        setUserName();

        updateLastSubmittedTime();

    }

    private void initGps() {

        mGps = new AppLocationService(HomeActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            } else {
                getGpsLocation();
            }

        } else {
            getGpsLocation();
        }

        if (mLatitude != 0.0 && mLongitude != 0.0) {
            mLocationPresenter.submitLocationToServer(Double.toString(mLatitude), Double.toString(mLongitude));
        } else {
            Toast.makeText(HomeActivity.this, "current location is not found yet", Toast.LENGTH_SHORT).show();
        }
    }


    private void setUserName(){
        String name=AppUtils.getStringFromPref(this,"username","John Doe");
        if(name.isEmpty()){
            name="John Doe";
        }
        edtName.setText(name);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLatitude != 0.0 && mLongitude != 0.0) {
            mLocationPresenter.submitLocationToServer(Double.toString(mLatitude), Double.toString(mLongitude));
        } else {
            Toast.makeText(HomeActivity.this, "current location is not found yet", Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.btn_submit)
    public void onClick() {
        if (mLatitude != 0.0 && mLongitude != 0.0) {
            mLocationPresenter.submitLocationToServer(Double.toString(mLatitude), Double.toString(mLongitude));
        } else {
            Toast.makeText(HomeActivity.this, "current location is not found yet", Toast.LENGTH_SHORT).show();
        }
    }


    @Subscribe
    public void onEventMainThread(Integer code) {

        final int statuscCode = code;
        runOnUiThread(new Runnable() {
            public void run() {
                if (statuscCode == 201) {
                    Date startTime = new Date();
                    AppUtils.putStringToPref(HomeActivity.this,"time",Long.toString(startTime.getTime()));
                    Toast.makeText(HomeActivity.this, "Location added successfully to the server", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "There is a problem in posting location to the server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(ErrorType errorType) {
        if (errorType == ErrorType.NO_INTERNET) {
            AppUtils.showDialog(this, R.string.internet_error);
        } else if (errorType == ErrorType.INTERNET_CONNECTED) {
            AppUtils.showDialog(this, R.string.internet_connected);
        }
    }

    @Subscribe
    public void onEventMainThread(Location location) {

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();

        tvLocationValue.setText(mLatitude + "," + mLongitude);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getGpsLocation();
            }
        }
    }

    private void getGpsLocation() {

        // check if GPS enabled
        if (mGps.canGetLocation()) {
            mLatitude = mGps.getLatitude();
            mLongitude = mGps.getLongitude();

            tvLocationValue.setText(mLatitude + "," + mLongitude);
        } else {
            mGps.showSettingsAlert();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.putStringToPref(this,"username",edtName.getText().toString());
        mEventBus.unregister(this);
        mGps.stopUsingGPS();
    }

    /**
     * method to update the last submitted time
     */
    private void updateLastSubmittedTime(){
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String date=AppUtils.getStringFromPref(HomeActivity.this,"time","");

                                long d1=Long.parseLong(date);

                                Date endDate=new Date();
                                long d2=endDate.getTime();

                                String time=AppUtils.convertTime(d2-d1);

                                tvLastSubmitted.setText("Last submitted "+ time +" ago");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }
}
