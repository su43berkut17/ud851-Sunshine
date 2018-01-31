package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView tv_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_weather=(TextView)findViewById(R.id.detailed_weather);

        // COMPLETED (2) Display the weather forecast that was passed from MainActivity
        Intent sent = getIntent();
        tv_weather.setText(sent.getStringExtra("weather"));
    }
}