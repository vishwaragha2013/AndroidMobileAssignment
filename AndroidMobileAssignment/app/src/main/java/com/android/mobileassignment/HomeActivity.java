package com.android.mobileassignment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        tvName.setPaintFlags(tvName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        tvLocation.setPaintFlags(tvLocation.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

    }


    @OnClick(R.id.btn_submit)
    public void onClick() {
    }
}
