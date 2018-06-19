package com.example.amathews707.weatherapp;

import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;
import android.util.Log;

public class MainScreen extends AppCompatActivity //implements LocationListener
{
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    Typeface weatherFont;

    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    /* Position */
    private static final int MINIMUM_TIME = 10000;  // 10s
    private static final int MINIMUM_DISTANCE = 50; // 50m
/*
    @Override
    public void onLocationChanged(Location location)
    {
        txtLat = (TextView) findViewById(R.id.textview1);
        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.d("Latitude","status"); }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_screen);

        txtLat = (TextView) findViewById(R.id.textview1); //it needs to be defined in .xml, gonna cut it later
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check if location services are permitted
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(  this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  }, MY_PERMISSION_ACCESS_FINE_LOCATION );
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_screen);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather-icons-master/font/weathericons-regular-webfont.ttf");

        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        pressure_field = (TextView)findViewById(R.id.pressure_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        /* Textviews for 5 day forecast
        dayOneWeatherIcon = (TextView)findViewById(R.id.day1);
        dayOneHigh = (TextView)findViewById(R.id.day1High);
        dayOneLow = (TextView)findViewById(R.id.day1Low);
        dayOneWeatherIcon.setTypeface(weatherFont);

        dayTwoWeatherIcon = (TextView)findViewById(R.id.day2);
        dayTwoWeatherIcon = (TextView)findViewById(R.id.day2);
        dayTwoWeatherIcon = (TextView)findViewById(R.id.day2);
        dayTwoWeatherIcon.setTypeface(weatherFont);

        dayThreeWeatherIcon = (TextView)findViewById(R.id.day3);
        dayThreeHigh = (TextView)findViewById(R.id.day3High);
        dayThreeLow = (TextView)findViewById(R.id.day3Low);
        dayThreeWeatherIcon.setTypeface(weatherFont);

        dayFourWeatherIcon = (TextView)findViewById(R.id.day4);
        dayFourHigh = (TextView)findViewById(R.id.day4High);
        dayFourLow = (TextView)findViewById(R.id.day4low);
        dayFourWeatherIcon.setTypeface(weatherFont);

        dayFiveWeatherIcon = (TextView)findViewById(R.id.day5);
        dayFiveHigh = (TextView)findViewById(R.id.day5High);
        dayFiveLow = (TextView)findViewById(R.id.day5Low);
        dayFiveWeatherIcon.setTypeface(weatherFont);
        */

        Functions.placeIdTask asyncTask =new Functions.placeIdTask(new Functions.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText("Humidity: "+weather_humidity);
                pressure_field.setText("Pressure: "+weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));



            }
        });
        asyncTask.execute("40.7831", "-73.9712"); //  asyncTask.execute("Latitude", "Longitude")

        //Listener for button and loading new area, then execute asyncTask with proper givens
    }
}

