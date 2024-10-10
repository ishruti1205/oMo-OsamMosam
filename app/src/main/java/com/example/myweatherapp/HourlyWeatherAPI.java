package com.example.myweatherapp;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.androdocs.httprequest.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HourlyWeatherAPI extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private final String API_Key;
    private long sunriseTimeMillis;
    private long sunsetTimeMillis;

    // Constructor to initialize MainActivity, API_KEY, sunrise and sunset times
    public HourlyWeatherAPI(MainActivity mainActivity, String apiKey, long sunrise, long sunset) {
        this.mainActivity = mainActivity;
        this.API_Key = apiKey;
        this.sunriseTimeMillis = sunrise;
        this.sunsetTimeMillis = sunset;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.getLoader_progress_bar().setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String city = params[0];
        return HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=" + API_Key);
    }

    @Override
    protected void onPostExecute(String result) {
        mainActivity.getLoader_progress_bar().setVisibility(View.GONE);

        if (result != null && !result.isEmpty()) {
            List<HourlyWeather> hourlyWeatherList = getHourlyWeatherData(result);

            // Pass JSON data to WeeklyWeatherAPI
            new WeeklyWeatherAPI(mainActivity, result).execute();

            HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(hourlyWeatherList);
            RecyclerView recyclerViewHourlyWeather = mainActivity.findViewById(R.id.recyclerViewHourlyWeather);

            // Set up RecyclerView with horizontal LinearLayoutManager
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewHourlyWeather.setLayoutManager(horizontalLayoutManager);
            recyclerViewHourlyWeather.setAdapter(hourlyWeatherAdapter);
        } else {
            Toast.makeText(mainActivity, "Error!", Toast.LENGTH_LONG).show();
        }
    }

    private List<HourlyWeather> getHourlyWeatherData(String jsonResponse) {
        List<HourlyWeather> hourlyWeatherList = new ArrayList<>();
        try {
            JSONObject hourlyJSONObj = new JSONObject(jsonResponse);
            JSONArray listArray = hourlyJSONObj.getJSONArray("list");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            long currentTimeMillis = System.currentTimeMillis();
            long oneDayLaterMillis = currentTimeMillis + 24 * 60 * 60 * 1000; // 24 hours in milliseconds

            boolean startIntervalFound = false;

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject hourlyObject = listArray.getJSONObject(i);
                String dt_txt = hourlyObject.getString("dt_txt");
                long hourlyTimeMillis = sdf.parse(dt_txt).getTime();

                // Find the interval where current time lies between this and the next interval
                if (!startIntervalFound) {
                    if (i < listArray.length() - 1) {
                        JSONObject nextHourlyObject = listArray.getJSONObject(i + 1);
                        String next_dt_txt = nextHourlyObject.getString("dt_txt");
                        long nextHourlyTimeMillis = sdf.parse(next_dt_txt).getTime();

                        if (currentTimeMillis >= hourlyTimeMillis && currentTimeMillis < nextHourlyTimeMillis) {
                            startIntervalFound = true;
                        }
                    } else if (currentTimeMillis >= hourlyTimeMillis) {
                        startIntervalFound = true;
                    }
                }

                // Add data if we have found the start interval and it is within the next 24 hours
                if (startIntervalFound && hourlyTimeMillis <= oneDayLaterMillis) {
                    String time = dt_txt.substring(11, 16);
                    JSONObject main = hourlyObject.getJSONObject("main");
                    String temperature = (int) Math.round(Double.parseDouble(main.getString("temp"))) + "°";
                    JSONArray weatherArray = hourlyObject.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);
                    String description = weather.getString("description");
                    int icon;

                    JSONObject sys = hourlyObject.getJSONObject("sys");
//                    if (!"n".equals(sys.getString("pod"))) {
//                        icon = mainActivity.getWeatherIcon(description, true);
//                    }
//                    else{
//                        icon = mainActivity.getWeatherIcon(description, false);
//                    }

                    // Determine whether current time is within the night period (between sunset and sunrise)
                    if (sunsetTimeMillis < sunriseTimeMillis) {
                        // Handle the case when sunset is before midnight and sunrise is the next morning
                        if (hourlyTimeMillis >= sunsetTimeMillis && hourlyTimeMillis < sunriseTimeMillis) {
                            // Night icons between sunset and next day's sunrise
                            icon = mainActivity.getWeatherIcon(description, false);
                        } else {
                            // Daytime icons in all other cases (before sunset or after sunrise)
                            icon = mainActivity.getWeatherIcon(description, true);
                        }
                    } else {
                        // Handle edge cases, like when times cross over a day boundary (e.g., after midnight)
                        if (hourlyTimeMillis >= sunsetTimeMillis || hourlyTimeMillis < sunriseTimeMillis) {
                            // Night icons between sunset and next day's sunrise
                            icon = mainActivity.getWeatherIcon(description, false);
                        } else {
                            // Daytime icons in all other cases (before sunset or after sunrise)
                            icon = mainActivity.getWeatherIcon(description, true);
                        }
                    }

                    // Add icons only if pod != "n"
//                    int icon = mainActivity.getWeatherIcon(description);
//                    String icon = weather.getString("icon");

                    // Construct the icon URL
//                    String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                    hourlyWeatherList.add(new HourlyWeather(time, icon, temperature));

                    // Adjust sunrise time if it is before the current time
                    if (sunriseTimeMillis < currentTimeMillis) {
                        sunriseTimeMillis += 24 * 60 * 60 * 1000; // Add 24 hours in milliseconds
                    }

                    // Insert sunrise card if within the interval
                    if (hourlyTimeMillis <= sunriseTimeMillis && sunriseTimeMillis < hourlyTimeMillis + 3 * 60 * 60 * 1000) {
                        String sunriseTime = MainActivity.formatTime(sunriseTimeMillis);
                        hourlyWeatherList.add(new HourlyWeather(sunriseTime, R.drawable.sunrise_yellow_img, temperature));
                    }

                    // Adjust sunset time if it is before the current time
                    if (sunsetTimeMillis < currentTimeMillis) {
                        sunsetTimeMillis += 24 * 60 * 60 * 1000; // Add 24 hours in milliseconds
                    }

                    // Insert sunset card if within the interval
                    if (hourlyTimeMillis <= sunsetTimeMillis && sunsetTimeMillis < hourlyTimeMillis + 3 * 60 * 60 * 1000) {
                        String sunsetTime = MainActivity.formatTime(sunsetTimeMillis);
                        hourlyWeatherList.add(new HourlyWeather(sunsetTime, R.drawable.sunset_yellow_img, temperature));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hourlyWeatherList;
    }

}