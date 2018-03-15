/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements
LoaderManager.LoaderCallbacks<Cursor>{
//      COMPLETED (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  COMPLETED (18) Create a String array containing the names of the desired data columns from our ContentProvider
    public static final String[] DATA_COL={WeatherContract.WeatherEntry.COLUMN_DATE,
        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
        WeatherContract.WeatherEntry.COLUMN_DEGREES,
        WeatherContract.WeatherEntry.COLUMN_PRESSURE,
    };
//  COMPLETED (19) Create constant int values representing each column name's position above
    public static final int COL_DATE=0;
    public static final int COL_ID=1;
    public static final int COL_MAX=2;
    public static final int COL_MIN=3;
    public static final int COL_HUM=4;
    public static final int COL_WIN=5;
    public static final int COL_DIR=6;
    public static final int COL_PRE=7;

//  COMPLETED (20) Create a constant int to identify our loader used in DetailActivity
    private final static int LOADER_ID=132;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  COMPLETED (15) Declare a private Uri field called mUri
    private Uri mUri;

//  COMPLETED (10) Remove the mWeatherDisplay TextView declaration


//  COMPLETED (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mWeatherDate;
    private TextView mWeatherDescription;
    private TextView mWeatherHigh;
    private TextView mWeatherLow;
    private TextView mWeatherHumidity;
    private TextView mWeatherWind;
    private TextView mWeatherPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      COMPLETED (12) Remove mWeatherDisplay TextView

//      COMPLETED (13) Find each of the TextViews by ID
        mWeatherDate=(TextView)findViewById(R.id.tvSelectedDate);
        mWeatherDescription=(TextView)findViewById(R.id.tvWeatherDescription);
        mWeatherHigh=(TextView)findViewById(R.id.tvHighTemp);
        mWeatherLow=(TextView)findViewById(R.id.tvLowTemp);
        mWeatherHumidity=(TextView)findViewById(R.id.tvHumidity);
        mWeatherWind=(TextView)findViewById(R.id.tvWind);
        mWeatherPressure=(TextView)findViewById(R.id.tvPressure);


//      COMPLETED (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            //      COMPLETED (16) Use getData to get a reference to the URI passed with this Activity's Intent
            mUri=intentThatStartedThisActivity.getData();

            if (mUri==null){
                //      COMPLETED (17) Throw a NullPointerException if that URI is null
                throw new NullPointerException("null uri: "+mUri);
            }
        }
//      COMPLETED (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

//  COMPLETED (22) Override onCreateLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //          COMPLETED (23) If the loader requested is our detail loader, return the appropriate CursorLoader

        switch (id){
            case LOADER_ID:
                return new CursorLoader(this,
                        mUri,
                        DATA_COL,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented "+id);
        }
    }



//  COMPLETED (24) Override onLoadFinished

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //      COMPLETED (25) Check before doing anything that the Cursor has valid data
        boolean hasData=false;
        if (data!=null && data.moveToFirst()){
            hasData=true;
        }

        if (!hasData){
            return;
        }

        //      COMPLETED (26) Display a readable data string
        long localDate=data.getLong(COL_DATE);
        String dateText=SunshineDateUtils.getFriendlyDateString(this,localDate,true);
        mWeatherDate.setText(dateText);

//      COMPLETED (27) Display the weather description (using SunshineWeatherUtils)
        int weatherId=data.getInt(COL_ID);
        String descrip=SunshineWeatherUtils.getStringForWeatherCondition(this,weatherId);
        mWeatherDescription.setText(descrip);

//      COMPLETED (28) Display the high temperature
        double highTemp=data.getDouble(COL_MAX);
        String sHighTemp=SunshineWeatherUtils.formatTemperature(this,highTemp);
        mWeatherHigh.setText(sHighTemp);

//      COMPLETED (29) Display the low temperature
        double lowTemp=data.getDouble(COL_MIN);
        String sLowTemp=SunshineWeatherUtils.formatTemperature(this,lowTemp);
        mWeatherLow.setText(sLowTemp);

//      COMPLETED (30) Display the humidity
        float humid=data.getFloat(COL_HUM);
        String sHumid=getString(R.string.format_humidity,humid);
        mWeatherHumidity.setText(sHumid);

//      COMPLETED (31) Display the wind speed and direction
        float windS=data.getFloat(COL_WIN);
        float windD=data.getFloat(COL_DIR);
        String sWind=SunshineWeatherUtils.getFormattedWind(this,windS,windD);
        mWeatherWind.setText(sWind);

//      COMPLETED (32) Display the pressure
        float press=data.getFloat(COL_PRE);
        String sPress= getString(R.string.format_pressure, press);
        mWeatherPressure.setText(sPress);

//      COMPLETED (33) Store a forecast summary in mForecastSummary
        mForecastSummary = String.format("%s - %s - %s/%s",
                                dateText, descrip, sHighTemp, sLowTemp);
    }




//  COMPLETED(34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}