package com.example.myweatherapp;

import android.os.AsyncTask;
import android.widget.Toast;
import com.androdocs.httprequest.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SearchCityAPI extends AsyncTask<String, Void, String> {
    private SearchCityActivity activity;
    private final String API_Key;
    private String city;

    public SearchCityAPI(SearchCityActivity activity, String API_Key, String city) {
        this.activity = activity;
        this.API_Key = API_Key;
        this.city = city;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        return HttpRequest.excuteGet("https://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=5&appid=" + API_Key);
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONArray currentJSONArr = new JSONArray(result);
            List<City> cities = new ArrayList<>();

            for (int i = 0; i < currentJSONArr.length(); i++) {
                JSONObject currentJSONObj = currentJSONArr.getJSONObject(i);
                String city = currentJSONObj.getString("name");
                String country = currentJSONObj.optString("country", "N/A");
                String state = currentJSONObj.optString("state", "N/A");
                Double latitude = currentJSONObj.getDouble("lat");
                Double longitude = currentJSONObj.getDouble("lon");

                cities.add(new City(city, state, country, latitude, longitude));
            }

            // Pass the results back to the activity
            activity.updateSearchResults(cities);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
