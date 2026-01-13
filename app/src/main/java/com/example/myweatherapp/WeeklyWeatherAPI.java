package com.example.myweatherapp;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyWeatherAPI extends AsyncTask<Void, Void, List<WeeklyWeather>> {

    private MainActivity mainActivity;
    private String jsonResponse;

    // Constructor
    public WeeklyWeatherAPI(MainActivity activity, String jsonResponse) {
        this.mainActivity = activity;
        this.jsonResponse = jsonResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.showLoader();
    }

    @Override
    protected List<WeeklyWeather> doInBackground(Void... params) {
        return getWeeklyWeatherData(jsonResponse);
    }

    @Override
    protected void onPostExecute(List<WeeklyWeather> weeklyWeatherList) {
        mainActivity.hideLoader();

        if (weeklyWeatherList != null && !weeklyWeatherList.isEmpty()) {
            WeeklyWeatherAdapter weeklyWeatherAdapter = new WeeklyWeatherAdapter(weeklyWeatherList);
            RecyclerView recyclerViewWeeklyWeather = mainActivity.findViewById(R.id.recyclerViewWeeklyWeather);

            // Set up RecyclerView with vertical LinearLayoutManager
            recyclerViewWeeklyWeather.setLayoutManager(new LinearLayoutManager(mainActivity));
            recyclerViewWeeklyWeather.setAdapter(weeklyWeatherAdapter);
        } else {
            Toast.makeText(mainActivity, "Error!", Toast.LENGTH_LONG).show();
        }
    }

    private List<WeeklyWeather> getWeeklyWeatherData(String jsonResponse) {
        List<WeeklyWeather> weeklyWeatherList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONArray listArray = jsonObj.getJSONArray("list");

            String currentDay = "";
            List<Double> minTemps = new ArrayList<>();
            List<Double> maxTemps = new ArrayList<>();
            List<String> icons = new ArrayList<>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date todayDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date tomorrowDate = calendar.getTime();

            String todayStr = dateFormat.format(todayDate);
            String tomorrowStr = dateFormat.format(tomorrowDate);

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject hourlyObject = listArray.getJSONObject(i);
                String dt_txt = hourlyObject.getString("dt_txt");
                String date = dt_txt.substring(0, 10);

                // Skip data before today
                if (isDateBeforeToday(date, todayStr)) {
                    continue;  // Skip this iteration
                }

                // Process previous day's data if a new date is encountered
                if (!date.equals(currentDay) && !currentDay.isEmpty()) {
                    // Process previous day's data
                    if (!isDateBeforeToday(date, todayStr)) {
                        String dayName = getDayName(currentDay, todayStr, tomorrowStr);

                        int icon = getMostFrequentIcon(icons);
//                        String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                        double minTemp = getMinValue(minTemps);
                        double maxTemp = getMaxValue(maxTemps);

                        if (dayName.equals("Today")) {
                            mainActivity.runOnUiThread(() -> {
                                mainActivity.getTemperature_min().setText((int) minTemp + "°");
                                mainActivity.getTemperature_max().setText((int) maxTemp + "°");
                            });
                        }

                        weeklyWeatherList.add(new WeeklyWeather(
                                currentDay.substring(5, 7), // Month
                                currentDay.substring(8, 10), // Date
                                dayName,
                                icon,
                                (int) minTemp + "°",
                                (int) maxTemp + "°"
                        ));
                    }

                    // Clear lists for the new day
                    minTemps.clear();
                    maxTemps.clear();
                    icons.clear();
                }

                // Update current day
                currentDay = date;

                // Add temperature values for processing
                JSONObject main = hourlyObject.getJSONObject("main");
                double temp = main.getDouble("temp");
                minTemps.add(temp);
                maxTemps.add(temp);

//                 Add icons only if pod != "n"
//                JSONObject sys = hourlyObject.getJSONObject("sys");
//                if (!"n".equals(sys.getString("pod"))) {
//                    JSONArray weatherArray = hourlyObject.getJSONArray("weather");
//                    icons.add(weatherArray.getJSONObject(0).getString("icon"));
//                }

                JSONArray weatherArray = hourlyObject.getJSONArray("weather");
                icons.add(weatherArray.getJSONObject(0).getString("description"));
            }

            // Process the last day's data
            if (!currentDay.isEmpty() && !isDateBeforeToday(currentDay, todayStr)) {
                String dayName = getDayName(currentDay, todayStr, tomorrowStr);
                int icon = getMostFrequentIcon(icons);
//                String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
                double minTemp = getMinValue(minTemps);
                double maxTemp = getMaxValue(maxTemps);

                weeklyWeatherList.add(new WeeklyWeather(
                        currentDay.substring(5, 7),
                        currentDay.substring(8, 10),
                        dayName,
                        icon,
                        (int) minTemp + "°",
                        (int) maxTemp + "°"
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklyWeatherList;
    }

    private boolean isDateBeforeToday(String date, String todayDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateToCheck = dateFormat.parse(date);
            Date today = dateFormat.parse(todayDate);
            return dateToCheck.before(today);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getDayName(String date, String todayDate, String tomorrowDate) {
        try {
            if (date.equals(todayDate)) {
                return "Today";
            } else if (date.equals(tomorrowDate)) {
                return "Tomorrow";
            } else {
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                Date dateObj = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
                return dayFormat.format(dateObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private double getMinValue(List<Double> values) {
        if (values.isEmpty()) return Double.NaN;
        double min = values.get(0);
        for (double val : values) {
            if (val < min) min = val;
        }
        return min;
    }

    private double getMaxValue(List<Double> values) {
        if (values.isEmpty()) return Double.NaN;
        double max = values.get(0);
        for (double val : values) {
            if (val > max) max = val;
        }
        return max;
    }

    private int getMostFrequentIcon(List<String> icons) {
        if (icons.isEmpty()) return mainActivity.getWeatherIcon("clouds", true);;

        String mostFrequentIcon = icons.get(0);
        int maxCount = 0;

        for (String icon : icons) {
            int count = 0;
            for (String i : icons) {
                if (i.equals(icon)) count++;
            }
            if (count > maxCount) {
                maxCount = count;
                mostFrequentIcon = icon;
            }
        }

        return mainActivity.getWeatherIcon(mostFrequentIcon, true);
    }
}
