package com.example.myweatherapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONObject;

public class CurrentWeatherAPI extends AsyncTask<String, Void, String> {
    private MainActivity activity;
    private final String API_Key;
    private Double lat, lon;

    public CurrentWeatherAPI(MainActivity activity, String API_Key, Double lat, Double lon) {
        this.activity = activity;
        this.API_Key = API_Key;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showLoader();
    }

    @Override
    protected String doInBackground(String... strings) {
        return HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon="+ lon + "&units=metric&appid=" + API_Key);
    }

    @Override
    protected void onPostExecute(String result) {
        try {
//            activity.hideLoader();

            JSONObject currentJSONObj = new JSONObject(result);
            JSONObject weather = currentJSONObj.getJSONArray("weather").getJSONObject(0);
            JSONObject main = currentJSONObj.getJSONObject("main");
            String description = weather.getString("description").toLowerCase();
            double visibility = currentJSONObj.getDouble("visibility");
            JSONObject wind = currentJSONObj.getJSONObject("wind");
            JSONObject sys = currentJSONObj.getJSONObject("sys");
            String city = currentJSONObj.getString("name");

            long sunriseTimeMillis = sys.getLong("sunrise") * 1000; // Convert to milliseconds
            long sunsetTimeMillis = sys.getLong("sunset") * 1000; // Convert to milliseconds
            long dt = currentJSONObj.getLong("dt")*1000; // Convert to milliseconds

            new CurrentUVIAQIAPI(activity, lat, lon, sunriseTimeMillis, sunsetTimeMillis).execute();
            new HourlyWeatherAPI(activity, API_Key, sunriseTimeMillis, sunsetTimeMillis).execute(lat.toString(), lon.toString());

            int bgIcon, bgColor;
            // Determine whether current time is within the night period (between sunset and sunrise)
            if (sunsetTimeMillis < sunriseTimeMillis) {
                // Handle the case when sunset is before midnight and sunrise is the next morning
                if (dt >= sunsetTimeMillis && dt < sunriseTimeMillis) {
                    // Night icons between sunset and next day's sunrise
                    bgIcon = activity.getWeatherIcon(description, false);
                    bgColor = ContextCompat.getColor(activity, R.color.darkBlueColor);
                } else {
                    // Daytime icons in all other cases (before sunset or after sunrise)
                    bgIcon = activity.getWeatherIcon(description, true);
                    bgColor = ContextCompat.getColor(activity, R.color.blueColor);
                }
            } else {
                // Handle edge cases, like when times cross over a day boundary (e.g., after midnight)
                if (dt >= sunsetTimeMillis || dt < sunriseTimeMillis) {
                    // Night icons between sunset and next day's sunrise
                    bgIcon = activity.getWeatherIcon(description, false);
                    bgColor = ContextCompat.getColor(activity, R.color.darkBlueColor);
                } else {
                    // Daytime icons in all other cases (before sunset or after sunrise)
                    bgIcon = activity.getWeatherIcon(description, true);
                    bgColor = ContextCompat.getColor(activity, R.color.blueColor);
                }
            }
            activity.getBgIcon().setImageResource(bgIcon);
            activity.getBgColor().setBackgroundColor(bgColor);

            activity.getCity().setText(city);
            activity.getCurrent_temperature().setText((int) Math.round(Double.parseDouble(main.getString("temp"))) + "°");
            activity.getWeather_description().setText(activity.capitalizeWords(weather.getString("description")) + " ");
//            activity.getTemperature_max().setText((int) Math.round(Double.parseDouble(main.getString("temp_max"))) + "°");
//            activity.getTemperature_min().setText((int) Math.round(Double.parseDouble(main.getString("temp_min"))) + "°");
            activity.getFeels_like_value().setText((int) Math.round(Double.parseDouble(main.getString("feels_like"))) + " °");
            activity.getHumidity_value().setText(main.getString("humidity"));
            activity.getWind_value().setText(String.valueOf((int) Math.round(Double.parseDouble(wind.getString("speed")))));
            activity.getAir_pressure_value().setText(main.getString("pressure"));
            activity.getVisibility_value().setText(String.valueOf(Math.round(visibility / 1000)));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("TESTING", e.getMessage());
        }
    }
}
