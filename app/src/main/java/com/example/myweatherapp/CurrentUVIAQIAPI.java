package com.example.myweatherapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.androdocs.httprequest.HttpRequest;
import org.json.JSONObject;

public class CurrentUVIAQIAPI extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private Double latitude;
    private Double longitude;
    private long sunriseTimeMillis;
    private long sunsetTimeMillis;

    // Constructor to initialize MainActivity and API_KEY
    public CurrentUVIAQIAPI(MainActivity activity, Double latitude, Double longitude, long sunrise, long sunset) {
        this.mainActivity = activity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunriseTimeMillis = sunrise;
        this.sunsetTimeMillis = sunset;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.hideLoader();
    }

    @Override
    protected String doInBackground(String... strings) {
        return HttpRequest.excuteGet("https://air-quality-api.open-meteo.com/v1/air-quality?latitude=" + latitude + "&longitude=" + longitude + "&current=us_aqi,uv_index");
    }

    @Override
    protected void onPostExecute(String result) {
        try {
//            mainActivity.hideLoader();

            JSONObject jsonObject = new JSONObject(result);
            JSONObject current = jsonObject.getJSONObject("current");

            String uvIndexStr = current.getString("uv_index");
            String aqiStr = current.getString("us_aqi");

            int uvIndex = (int) Math.round(Double.parseDouble(uvIndexStr));
            int aqi = Integer.parseInt(aqiStr);

            mainActivity.getUV_value().setText(String.valueOf(uvIndex));
            mainActivity.getAQI_value().setText(String.valueOf(aqi));

            // UV Index Summary
            String uvSummary = getUVIndexSummary(uvIndex);
            mainActivity.getUV_summary().setText(uvSummary);

            // AQI Description and Paragraph
            String[] aqiDetails = getAQIDetails(aqi);
            mainActivity.getAQI_description().setText(aqiDetails[0]);
            mainActivity.getAQI_paragraph().setText(aqiDetails[1]);
            mainActivity.getAQI_summary().setText(aqiDetails[0] + " " + aqi);

            // Update AQI Progress Bar
            updateAqiProgressBar(aqi);

            // Update Sun Progress Bar
            updateSunProgressBar();

        } catch (Exception e) {
            Toast.makeText(mainActivity, "Unexpected Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String getUVIndexSummary(int uvIndex) {
        if (uvIndex <= 2) return " Low";
        if (uvIndex <= 5) return " Moderate";
        if (uvIndex <= 7) return " High";
        if (uvIndex <= 10) return " Very High";
        return " Extreme";
    }

    private String[] getAQIDetails(int aqi) {
        String aqiDescription;
        String aqiParagraph;
        if (aqi <= 50) {
            aqiDescription = "Good";
            aqiParagraph = "Air quality is good.\nEnjoy your usual outdoor activities.";
        } else if (aqi <= 100) {
            aqiDescription = "Satisfactory";
            aqiParagraph = "Air quality is acceptable.\nHowever, unusually sensitive individuals may experience minor to moderate symptoms from long-term exposure.";
        } else if (aqi <= 150) {
            aqiDescription = "Moderate";
            aqiParagraph = "Air quality is acceptable.\nHowever, there may be a moderate health concern for a very small number of people who are unusually sensitive to air pollution. May cause breathing discomfort to people with e.g. asthma and discomfort to people with heart disease, children and seniors.";
        } else if (aqi <= 200) {
            aqiDescription = "Poor";
            aqiParagraph = "Air quality is poor.\nEveryone may begin to experience health effects. Members of sensitive groups may experience more serious health effects. May cause breathing discomfort at prolonged exposure and discomfort to people with heart disease at short exposure.";
        } else if (aqi <= 300) {
            aqiDescription = "Very Poor";
            aqiParagraph = "Air quality is very poor.\nMay cause respiratory illness at prolonged exposure. People with lung/heart disease may be affected more severely.";
        } else {
            aqiDescription = "Severe";
            aqiParagraph = "Air quality is extremely poor.\nMay impact respiratory even on healthy people and cause serious health impacts on people with lung/heart disease. The health impacts may be experienced even during light physical activity.";
        }
        return new String[]{aqiDescription, aqiParagraph};
    }

    private void updateAqiProgressBar(int aqi) {
        mainActivity.getAqi_progress_bar().setProgress(aqi);
        mainActivity.getAqi_progress_bar().post(() -> {
            int indicatorPosition = (int) ((aqi / (float) mainActivity.getAqi_progress_bar().getMax()) * mainActivity.getAqi_progress_bar().getWidth());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainActivity.getAqi_indicator().getLayoutParams();
            params.leftMargin = indicatorPosition - (mainActivity.getAqi_indicator().getWidth() / 2);
            mainActivity.getAqi_indicator().setLayoutParams(params);
        });
    }


    private void updateSunProgressBar() {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis <= sunriseTimeMillis) {
            // Before sunrise: progress from sunset to next sunrise
            updateSunsetToSunriseProgress();
            Log.d("SunIndicator", "currentTimeMillis < sunriseTimeMillis");

        } else if (currentTimeMillis >= sunsetTimeMillis) {
            // After sunset: progress from sunset to next sunrise
            updateSunsetToSunriseProgress();
            Log.d("SunIndicator", "currentTimeMillis >= sunsetTimeMillis");
        } else {
            // Daytime: progress from sunrise to sunset
            updateSunriseToSunsetProgress();
            Log.d("SunIndicator", "else part");
        }
    }

    private void updateSunriseToSunsetProgress() {
        long currentTimeMillis = System.currentTimeMillis();
        long totalDuration = sunsetTimeMillis - sunriseTimeMillis;
        long elapsedTime = currentTimeMillis - sunriseTimeMillis;
        int sunProgress = (int) ((elapsedTime / (float) totalDuration) * 100);

        // Update UI for sunrise to sunset
        updateSunCardView(MainActivity.formatTime(sunriseTimeMillis), MainActivity.formatTime(sunsetTimeMillis));

        // Update progress bar
        mainActivity.getSun_progress_bar().setMax(100);
        mainActivity.getSun_progress_bar().setProgress(sunProgress);

        // Update sun indicator position
        mainActivity.getSun_progress_bar().post(() -> {
            int sunIndicatorPosition = (int) ((sunProgress / 100.0) * mainActivity.getSun_progress_bar().getWidth());
            RelativeLayout.LayoutParams sunParams = (RelativeLayout.LayoutParams) mainActivity.getSun_indicator().getLayoutParams();
            sunParams.leftMargin = sunIndicatorPosition - (mainActivity.getSun_indicator().getWidth() / 2);
            mainActivity.getSun_indicator().setLayoutParams(sunParams);
        });

        // Log statements for debugging
        Log.d("SunIndicator", "Current Time (Millis): " + currentTimeMillis);
        Log.d("SunIndicator", "Sunrise Time (Millis): " + sunriseTimeMillis);
        Log.d("SunIndicator", "Sunset Time (Millis): " + sunsetTimeMillis);
        Log.d("SunIndicator", "Total Duration: " + totalDuration);
        Log.d("SunIndicator", "Elapsed Time: " + elapsedTime);
        Log.d("SunIndicator", "Sun Progress: " + sunProgress);
    }

    private void updateSunsetToSunriseProgress() {
        long currentTimeMillis = System.currentTimeMillis();

        long totalDuration;
        long elapsedTime;

        if (currentTimeMillis >= sunsetTimeMillis) {
            // Current time is between sunset and sunrise on the next day
            totalDuration = (24 * 60 * 60 * 1000) + sunriseTimeMillis - sunsetTimeMillis;
            elapsedTime = currentTimeMillis - sunsetTimeMillis;
        } else {
            // Current time is before sunset
            totalDuration = (24 * 60 * 60 * 1000) + sunriseTimeMillis - sunsetTimeMillis;
            elapsedTime = (24 * 60 * 60 * 1000) + currentTimeMillis - sunsetTimeMillis;
        }

        // Calculate progress percentage
        int sunProgress = (int) ((elapsedTime / (float) totalDuration) * 100);
        sunProgress = Math.max(0, Math.min(sunProgress, 100));

        // Update UI elements
        updateSunCardView(R.drawable.moon_img, R.drawable.sunset_img, "Sunset", MainActivity.formatTime(sunsetTimeMillis),
                R.drawable.sunrise_img, "Sunrise", MainActivity.formatTime(sunriseTimeMillis));

        mainActivity.getSun_progress_bar().setMax(100);
        mainActivity.getSun_progress_bar().setProgress(sunProgress);

        final int progressBarWidth = mainActivity.getSun_progress_bar().getWidth();
        final int sunIndicatorWidth = mainActivity.getSun_indicator().getWidth();
        final int sunIndicatorPosition = (int) ((sunProgress / 100.0) * (progressBarWidth - sunIndicatorWidth));

        // Ensure sunIndicatorPosition is within bounds
        int leftMargin = Math.max(0, Math.min(sunIndicatorPosition, progressBarWidth - sunIndicatorWidth));

        mainActivity.getSun_progress_bar().post(() -> {
            RelativeLayout.LayoutParams sunParams = (RelativeLayout.LayoutParams) mainActivity.getSun_indicator().getLayoutParams();
            sunParams.leftMargin = leftMargin;
            mainActivity.getSun_indicator().setLayoutParams(sunParams);

            // Log statements for debugging
            Log.d("SunIndicator", "Current Time (Millis): " + currentTimeMillis);
            Log.d("SunIndicator", "Sunrise Time (Millis): " + sunriseTimeMillis);
            Log.d("SunIndicator", "Sunset Time (Millis): " + sunsetTimeMillis);
            Log.d("SunIndicator", "Total Duration: " + totalDuration);
            Log.d("SunIndicator", "Elapsed Time: " + elapsedTime);
            Log.d("SunIndicator", "Progress Bar Width: " + progressBarWidth);
            Log.d("SunIndicator", "Sun Indicator Width: " + sunIndicatorWidth);
            Log.d("SunIndicator", "Sun Indicator Position: " + sunIndicatorPosition);
            Log.d("SunIndicator", "Left Margin: " + leftMargin);
        });
        Log.d("SunIndicator", "Sun Progress: " + sunProgress);
    }

    private void updateSunCardView(int indicatorResId, int leftIconResId, String leftText, String leftTimeText,
                                   int rightIconResId, String rightText, String rightTimeText) {
        mainActivity.getSun_indicator().setImageResource(indicatorResId);
        mainActivity.getLeft_sun_img().setImageResource(leftIconResId);
        mainActivity.getRight_sun_img().setImageResource(rightIconResId);
        mainActivity.getLeft_sun_text().setText(leftText);
        mainActivity.getRight_sun_text().setText(rightText);
        mainActivity.getLeft_sun_time().setText(leftTimeText);
        mainActivity.getRight_sun_time().setText(rightTimeText);
    }

    private void updateSunCardView(String leftTimeText,String rightTimeText) {
        mainActivity.getLeft_sun_time().setText(leftTimeText);
        mainActivity.getRight_sun_time().setText(rightTimeText);
    }

}
