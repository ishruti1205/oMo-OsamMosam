package com.example.myweatherapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CurrentWeather extends AsyncTask<String, Void, String> {
    private MainActivity activity;
    private final String API_Key;
    private String city;

    public CurrentWeather(MainActivity activity, String API_Key, String city) {
        this.activity = activity;
        this.API_Key = API_Key;
        this.city = city;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.getLoader_progress_bar().setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {
        return HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + API_Key);
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            activity.getLoader_progress_bar().setVisibility(View.GONE);

            JSONObject currentJSONObj = new JSONObject(result);
            JSONObject weather = currentJSONObj.getJSONArray("weather").getJSONObject(0);
            JSONObject coord = currentJSONObj.getJSONObject("coord");
            JSONObject main = currentJSONObj.getJSONObject("main");
            double visibility = currentJSONObj.getDouble("visibility");
            JSONObject wind = currentJSONObj.getJSONObject("wind");
            JSONObject sys = currentJSONObj.getJSONObject("sys");

            Double longitude = coord.getDouble("lon");
            Double latitude = coord.getDouble("lat");

            String city = activity.getCity().getText().toString();

            long sunrise_time = sys.getLong("sunrise") * 1000; // Convert to milliseconds
            long sunset_time = sys.getLong("sunset") * 1000; // Convert to milliseconds

            new CurrentUVIAQI(activity, latitude, longitude, sunrise_time, sunset_time).execute();
            new HourlyWeatherAPI(activity, API_Key, sunrise_time, sunset_time).execute(city);

            activity.getCurrent_temperature().setText((int) Math.round(Double.parseDouble(main.getString("temp"))) + "°");
            activity.getWeather_description().setText(activity.capitalizeWords(weather.getString("description")) + " ");
//            activity.getTemperature_max().setText((int) Math.round(Double.parseDouble(main.getString("temp_max"))) + "°");
//            activity.getTemperature_min().setText((int) Math.round(Double.parseDouble(main.getString("temp_min"))) + "°");
            activity.getFeels_like_value().setText((int) Math.round(Double.parseDouble(main.getString("feels_like"))) + " °");
            activity.getHumidity_value().setText(main.getString("humidity"));
            activity.getWind_value().setText(String.valueOf((int) Math.round(Double.parseDouble(wind.getString("speed")))));
            activity.getAir_pressure_value().setText(main.getString("pressure"));
            activity.getVisibility_value().setText(String.valueOf(Math.round(visibility / 1000)));

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
