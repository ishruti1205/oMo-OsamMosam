package com.example.myweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWeeklyWeather;
    private WeeklyWeatherAdapter weeklyWeatherAdapter;
    private List<WeeklyWeather> weeklyWeatherList;

    private final String API_Key = "df6e7472c74227efcecc770beb4107b5";
    private LinearLayout background;
    private TextView city;
    private TextView current_temperature;
    private TextView weather_description;
    private TextView temperature_max;
    private TextView temperature_min;
    private TextView aqi_value;
    private TextView aqi_description;
    private TextView aqi_summary;
    private TextView aqi_paragraph;
    private TextView uv_value;
    private TextView uv_summary;
    private TextView feels_like_value;
    private TextView humidity_value;

    public RecyclerView getRecyclerViewWeeklyWeather() {
        return recyclerViewWeeklyWeather;
    }

    public void setRecyclerViewWeeklyWeather(RecyclerView recyclerViewWeeklyWeather) {
        this.recyclerViewWeeklyWeather = recyclerViewWeeklyWeather;
    }

    public WeeklyWeatherAdapter getWeeklyWeatherAdapter() {
        return weeklyWeatherAdapter;
    }

    public void setWeeklyWeatherAdapter(WeeklyWeatherAdapter weeklyWeatherAdapter) {
        this.weeklyWeatherAdapter = weeklyWeatherAdapter;
    }

    public List<WeeklyWeather> getWeeklyWeatherList() {
        return weeklyWeatherList;
    }

    public void setWeeklyWeatherList(List<WeeklyWeather> weeklyWeatherList) {
        this.weeklyWeatherList = weeklyWeatherList;
    }

    public String getAPI_Key() {
        return API_Key;
    }

    public LinearLayout getBackground() {
        return background;
    }

    public void setBackground(LinearLayout background) {
        this.background = background;
    }

    public TextView getCity() {
        return city;
    }

    public void setCity(TextView city) {
        this.city = city;
    }

    public TextView getCurrent_temperature() {
        return current_temperature;
    }

    public void setCurrent_temperature(TextView current_temperature) {
        this.current_temperature = current_temperature;
    }

    public TextView getWeather_description() {
        return weather_description;
    }

    public void setWeather_description(TextView weather_description) {
        this.weather_description = weather_description;
    }

    public TextView getTemperature_max() {
        return temperature_max;
    }

    public void setTemperature_max(TextView temperature_max) {
        this.temperature_max = temperature_max;
    }

    public TextView getTemperature_min() {
        return temperature_min;
    }

    public void setTemperature_min(TextView temperature_min) {
        this.temperature_min = temperature_min;
    }

    public TextView getAQI_value() {
        return aqi_value;
    }

    public void setAQI_value(TextView aqi_value) {
        this.aqi_value = aqi_value;
    }

    public TextView getAQI_description() {
        return aqi_description;
    }

    public void setAQI_description(TextView aqi_description) {
        this.aqi_description = aqi_description;
    }

    public TextView getAQI_summary() {
        return aqi_summary;
    }

    public void setAQI_summary(TextView aqi_summary) {
        this.aqi_summary = aqi_summary;
    }

    public TextView getAQI_paragraph() {
        return aqi_paragraph;
    }

    public void setAqi_paragraph(TextView aqi_paragraph) {
        this.aqi_paragraph = aqi_paragraph;
    }

    public TextView getUV_value() {
        return uv_value;
    }

    public void setUV_value(TextView uv_value) {
        this.uv_value = uv_value;
    }

    public TextView getUV_summary() {
        return uv_summary;
    }

    public void setUv_summary(TextView uv_summary) {
        this.uv_summary = uv_summary;
    }

    public TextView getFeels_like_value() {
        return feels_like_value;
    }

    public void setFeels_like_value(TextView feels_like_value) {
        this.feels_like_value = feels_like_value;
    }

    public TextView getHumidity_value() {
        return humidity_value;
    }

    public void setHumidity_value(TextView humidity_value) {
        this.humidity_value = humidity_value;
    }

    public TextView getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(TextView wind_direction) {
        this.wind_direction = wind_direction;
    }

    public TextView getWind_value() {
        return wind_value;
    }

    public void setWind_value(TextView wind_value) {
        this.wind_value = wind_value;
    }

    public TextView getAir_pressure_value() {
        return air_pressure_value;
    }

    public void setAir_pressure_value(TextView air_pressure_value) {
        this.air_pressure_value = air_pressure_value;
    }

    public TextView getVisibility_value() {
        return visibility_value;
    }

    public void setVisibility_value(TextView visibility_value) {
        this.visibility_value = visibility_value;
    }

    public TextView getLeft_sun_text() {
        return left_sun_text;
    }

    public void setLeft_sun_text(TextView left_sun_text) {
        this.left_sun_text = left_sun_text;
    }

    public TextView getRight_sun_text() {
        return right_sun_text;
    }

    public void setRight_sun_text(TextView right_sun_text) {
        this.right_sun_text = right_sun_text;
    }

    public TextView getLeft_sun_time() {
        return left_sun_time;
    }

    public void setLeft_sun_time(TextView left_sun_time) {
        this.left_sun_time = left_sun_time;
    }

    public TextView getRight_sun_time() {
        return right_sun_time;
    }

    public void setRight_sun_time(TextView right_sun_time) {
        this.right_sun_time = right_sun_time;
    }

    public ImageView getLocation_icon() {
        return location_icon;
    }

    public void setLocation_icon(ImageView location_icon) {
        this.location_icon = location_icon;
    }

    public ImageView getSettings_icon() {
        return settings_icon;
    }

    public void setSettings_icon(ImageView settings_icon) {
        this.settings_icon = settings_icon;
    }

    public ImageView getAqi_indicator() {
        return aqi_indicator;
    }

    public void setAqi_indicator(ImageView aqi_indicator) {
        this.aqi_indicator = aqi_indicator;
    }

    public ImageView getSun_indicator() {
        return sun_indicator;
    }

    public void setSun_indicator(ImageView sun_indicator) {
        this.sun_indicator = sun_indicator;
    }

    public ImageView getLeft_sun_img() {
        return left_sun_img;
    }

    public void setLeft_sun_img(ImageView left_sun_img) {
        this.left_sun_img = left_sun_img;
    }

    public ImageView getRight_sun_img() {
        return right_sun_img;
    }

    public void setRight_sun_img(ImageView right_sun_img) {
        this.right_sun_img = right_sun_img;
    }

    public ProgressBar getAqi_progress_bar() {
        return aqi_progress_bar;
    }

    public void setAqi_progress_bar(ProgressBar aqi_progress_bar) {
        this.aqi_progress_bar = aqi_progress_bar;
    }

    public ProgressBar getSun_progress_bar() {
        return sun_progress_bar;
    }

    public void setSun_progress_bar(ProgressBar sun_progress_bar) {
        this.sun_progress_bar = sun_progress_bar;
    }

    public ProgressBar getLoader_progress_bar() {
        return loader_progress_bar;
    }

    public void setLoader_progress_bar(ProgressBar loader_progress_bar) {
        this.loader_progress_bar = loader_progress_bar;
    }

    private TextView wind_direction;
    private TextView wind_value;
    private TextView air_pressure_value;
    private TextView visibility_value;
    private TextView left_sun_text;
    private TextView right_sun_text;
    private TextView left_sun_time;
    private TextView right_sun_time;
    private ImageView location_icon, settings_icon, aqi_indicator,
            sun_indicator, left_sun_img, right_sun_img;
    private ProgressBar aqi_progress_bar, sun_progress_bar, loader_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        background = findViewById(R.id.root_layout);
        city = findViewById(R.id.city);
        current_temperature = findViewById(R.id.current_temperature);
        weather_description = findViewById(R.id.weather_description);
        temperature_max = findViewById(R.id.temperature_max);
        temperature_min = findViewById(R.id.temperature_min);
        aqi_value = findViewById(R.id.aqi_value);
        aqi_description = findViewById(R.id.aqi_description);
        aqi_summary = findViewById(R.id.aqi_summary);
        aqi_paragraph = findViewById(R.id.aqi_paragraph);
        uv_value = findViewById(R.id.uv_value);
        uv_summary = findViewById(R.id.uv_summary);
        feels_like_value = findViewById(R.id.feels_like_value);
        humidity_value = findViewById(R.id.humidity_value);
        wind_direction = findViewById(R.id.wind_direction);
        wind_value = findViewById(R.id.wind_value);
        air_pressure_value = findViewById(R.id.air_pressure_value);
        visibility_value = findViewById(R.id.visibility_value);

        location_icon = findViewById(R.id.location_icon);
        settings_icon = findViewById(R.id.settings_icon);
        aqi_indicator = findViewById(R.id.aqi_indicator);
        sun_indicator = findViewById(R.id.sun_indicator);
        aqi_progress_bar = findViewById(R.id.aqi_progress_bar);

        left_sun_img = findViewById(R.id.left_sun_img);
        right_sun_img = findViewById(R.id.right_sun_img);
        sun_progress_bar = findViewById(R.id.sun_progress_bar);
        left_sun_text = findViewById(R.id.left_sun_text);
        right_sun_text = findViewById(R.id.right_sun_text);
        left_sun_time = findViewById(R.id.left_sun_time);
        right_sun_time = findViewById(R.id.right_sun_time);

        loader_progress_bar = findViewById(R.id.loader_progress_bar);

        new CurrentWeather(MainActivity.this, getAPI_Key(), getCity().getText().toString()).execute();

        location_icon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CityActivity.class);
            startActivity(intent);
        });
    }

    public static String formatTime(long timeMillis) {
        // Convert milliseconds to hours and minutes
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = new Date(timeMillis);
        return sdf.format(date);
    }

    public int getWeatherIcon(String description) {
        if (description.contains("clear sky")) {
            return R.drawable.sun_img;
//            return R.drawable.moon_img;
        }
        else if (description.contains("few clouds")) {
            return R.drawable.partly_cloudy_day_img;
//            return R.drawable.partly_cloudy_night_img;
        }
        else if (description.contains("scattered clouds")) {
            return R.drawable.partly_cloudy_day_img;
//            return R.drawable.partly_cloudy_night_img;
        }
        else if (description.contains("broken clouds") || description.contains("overcast clouds")) {
            return R.drawable.cloudy_img;
        }
        else if (description.contains("thunderstorm")) {
            return R.drawable.thunderstorm_img;
        }
        else if (description.contains("drizzle")) {
            return R.drawable.moderate_rain_img;
        }
        else if (description.contains("heavy") && description.contains("rain")) {
            return R.drawable.heavy_rain_img;
        }
        else if (description.contains("shower rain")) {
            return R.drawable.light_rain_img;
        }
        else if (description.contains("rain")) {
            return R.drawable.moderate_rain_img;
        }
        else if (description.contains("snow") || description.contains("sleet") || description.contains("freezing rain")) {
                return R.drawable.snow_img;
        }
        else {
            // main = atmosphere
            return R.drawable.mist_img;
        }
    }

    public int getWeatherIcon(String description, boolean day) {
        if (day){
            if (description.contains("clear sky")) {
                return R.drawable.sun_img;
            }
            else if (description.contains("few clouds")) {
                return R.drawable.partly_cloudy_day_img;
            }
            else if (description.contains("scattered clouds")) {
                return R.drawable.partly_cloudy_day_img;
            }
            else if (description.contains("broken clouds") || description.contains("overcast clouds")) {
                return R.drawable.cloudy_img;
            }
            else if (description.contains("thunderstorm")) {
                return R.drawable.thunderstorm_img;
            }
            else if (description.contains("drizzle")) {
                return R.drawable.moderate_rain_img;
            }
            else if (description.contains("heavy") && description.contains("rain")) {
                return R.drawable.heavy_rain_img;
            }
            else if (description.contains("shower rain")) {
                return R.drawable.light_rain_img;
            }
            else if (description.contains("snow") || description.contains("sleet") || description.contains("freezing rain")) {
                return R.drawable.snow_img;
            }
            else if (description.contains("rain")) {
                return R.drawable.moderate_rain_img;
            }
        }
        else{
            if (description.contains("clear sky")) {
                return R.drawable.moon_img;
            }
            else if (description.contains("few clouds")) {
                return R.drawable.partly_cloudy_night_img;
            }
            else if (description.contains("scattered clouds")) {
                return R.drawable.partly_cloudy_night_img;
            }
            else if (description.contains("broken clouds") || description.contains("overcast clouds")) {
                return R.drawable.cloudy_img;
            }
            else if (description.contains("thunderstorm")) {
                return R.drawable.thunderstorm_img;
            }
            else if (description.contains("drizzle")) {
                return R.drawable.moderate_rain_img;
            }
            else if (description.contains("heavy") && description.contains("rain")) {
                return R.drawable.heavy_rain_img;
            }
            else if (description.contains("shower rain")) {
                return R.drawable.light_rain_img;
            }
            else if (description.contains("snow") || description.contains("sleet") || description.contains("freezing rain")) {
                return R.drawable.snow_img;
            }
            else if (description.contains("rain")) {
                return R.drawable.moderate_rain_img;
            }

        }
        // main = atmosphere
        return R.drawable.mist_img;
    }


    public static String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] words = str.split("\\s+");
        StringBuilder capitalizedWords = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalizedWords.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        // Remove the trailing space
        return capitalizedWords.toString().trim();

    }

}