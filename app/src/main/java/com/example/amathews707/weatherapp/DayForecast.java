package com.example.amathews707.weatherapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayForecast {

    private static SimpleDateFormat date = new SimpleDateFormat("EEE");
    public ForecastTemp forecastTemp = new ForecastTemp();
    public long timestamp;

    public class ForecastTemp
    {
        public String day;
        public String min;
        public String max;
        public String iconText;
    }

    public String getStringDate() {
        return date.format(new Date(timestamp));
    }
}