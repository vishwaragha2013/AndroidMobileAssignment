package com.android.mobileassignment.view;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mobileassignment.R;
import com.android.mobileassignment.data.ErrorType;
import com.android.mobileassignment.presenter.LocationPresenter;
import com.android.mobileassignment.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        tvName.setPaintFlags(tvName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        tvLocation.setPaintFlags(tvLocation.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        mLocationPresenter=new LocationPresenter(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mEventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @OnClick(R.id.btn_submit)
    public void onClick() {
        mLocationPresenter.submitLocationToServer("456","56565");
    }


    @Subscribe
    public void onEventMainThread(Integer code) {


        final int statuscCode = code;
        runOnUiThread(new Runnable() {
            public void run() {
                if (statuscCode == 201) {
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

}
