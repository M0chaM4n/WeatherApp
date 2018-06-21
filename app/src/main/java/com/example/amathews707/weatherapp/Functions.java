package com.example.amathews707.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Functions
{
    private static final String openWeatherURL =  "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=imperial";
    private static final String openWeatherForecastURL = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&units=metric&cnt=5"; //might need to add in IT&mode=json
    private static final String apiKey = "3e4ce794c0ff3c44b05ded9e05e48e9c";

    //Get icon representative of weather conditions currently
    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }



    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8, WeatherForecast forecast);
    }


    public static class placeIdTask extends AsyncTask<String, Void, JSONObject[]> {

        public AsyncResponse delegate = null;

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        //longitude and latitude coordinates are passed here, jsonWeathergets a weather Json with said coordinates and passes result to onPostExecute
        @Override
        protected JSONObject[] doInBackground(String... params) {

            JSONObject jsonWeather = null;
            JSONObject fiveDay = null;

            JSONObject elements[] = {jsonWeather, fiveDay};
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }

            try {
                fiveDay = getforecastJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }

            return elements;
        }

        //broken because json is now an array
        @Override
        protected void onPostExecute(JSONObject[] json)
        {
            //Uses the Json to pull the current weather for today
            try
            {
                if(json != null)
                {
                    JSONObject details = json[0].getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json[0].getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    WeatherForecast forecast = new WeatherForecast();

                    String city = json[0].getString("name").toUpperCase(Locale.US) + ", " + json[0].getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.2f", main.getDouble("temp"))+ "°";
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";
                    String updatedOn = df.format(new Date(json[0].getLong("dt")*1000));
                    String iconText = setWeatherIcon(details.getInt("id"),
                            json[0].getJSONObject("sys").getLong("sunrise") * 1000,
                            json[0].getJSONObject("sys").getLong("sunset") * 1000);

                    JSONArray days = json[1].getJSONArray("list");
                    // We traverse all the array and parse the data
                    for (int i = 0; i < days.length(); i++)
                    {
                        //JSONObject jDayForecast = days.getJSONObject(i); Don't think this is even needed...

                        // Now we have the json object so we can extract the data
                        DayForecast currentDay = new DayForecast();

                        // We retrieve the timestamp (dt)
                        currentDay.timestamp = days.getJSONObject(i).getLong("dt");

                        // Temp is an object
                        JSONObject jTempObj = days.getJSONObject(i).getJSONObject("temp");

                        currentDay.forecastTemp.iconText = setWeatherIcon(jTempObj.getInt("id"),
                                jTempObj.getJSONObject("sys").getLong("sunrise") * 1000,
                                jTempObj.getJSONObject("sys").getLong("sunset") * 1000);
                        //currentDay.forecastTemp.day = Double.toString(jTempObj.getDouble("day"));  The timestamp has a function defined in DayForecast that will output the first 3 letters of the day
                        currentDay.forecastTemp.min =  String.format("%.1f", Double.toString(jTempObj.getDouble("min")));
                        currentDay.forecastTemp.max =  String.format("%.1f", Double.toString(jTempObj.getDouble("max")));
                        forecast.addForecast(currentDay);
                    }

                    delegate.processFinish(city, description, temperature, humidity, pressure, updatedOn, iconText, ""+ (json[0].getJSONObject("sys").getLong("sunrise") * 1000),forecast);
                }
            }
            catch (JSONException e)
            {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }

        }
    }

    public static JSONObject getWeatherJSON(String lat, String lon)
    {
        try {
            URL url = new URL(String.format(openWeatherURL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", apiKey);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }

    public static JSONObject getforecastJSON(String lat, String lon)
    {
        try {
            URL url = new URL(String.format(openWeatherForecastURL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", apiKey);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }

}
