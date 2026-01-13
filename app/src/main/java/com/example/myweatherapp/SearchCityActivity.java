package com.example.myweatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchCityActivity extends AppCompatActivity {
    private final MainActivity mainActivity = new MainActivity();
    private TextView cancelAddCity;
    private RecyclerView searchResults;
    private SearchCityAdapter searchCityAdapter;
    private List<City> cities = new ArrayList<>();
    private SearchView searchView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_city);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Fetch padding defined in XML
            int currentPaddingLeft = v.getPaddingLeft();
            int currentPaddingTop = v.getPaddingTop();
            int currentPaddingRight = v.getPaddingRight();
            int currentPaddingBottom = v.getPaddingBottom();

            // Adjust padding by adding system insets to the XML-defined padding
            v.setPadding(
                    currentPaddingLeft + systemBars.left, // Add inset to the existing left padding
                    50 + systemBars.top,   // Add inset to the existing top padding
                    currentPaddingRight + systemBars.right, // Add inset to the existing right padding
                    systemBars.bottom // Add inset to the existing bottom padding
            );

            return insets;
        });

        // Back button click listener
        cancelAddCity = findViewById(R.id.cancelAddCity);
        cancelAddCity.setOnClickListener(v -> finish());

        // Load addedCities from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CityPrefs", MODE_PRIVATE);
        String addedCitiesJson = sharedPreferences.getString("addedCities", "[]");
        Type type = new TypeToken<ArrayList<City>>() {}.getType();
        ArrayList<City> addedCities = new Gson().fromJson(addedCitiesJson, type);

        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.search_bar);
        searchResults = findViewById(R.id.searchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(this));

        searchCityAdapter = new SearchCityAdapter(cities, city -> {
            boolean cityExists = false;
            for (City addedCity : addedCities) {
                if (addedCity.getName().equalsIgnoreCase(city.getName())) {
                    cityExists = true;
                    break;
                }
            }

            if (!cityExists) {
                addedCities.add(city); // Add city to the list
                Toast.makeText(this, "City added: " + city.getName(), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("addedCities", new Gson().toJson(addedCities));
                editor.apply();
            } else {
                Toast.makeText(this, "City already exists.", Toast.LENGTH_SHORT).show();
            }
        });
        searchResults.setAdapter(searchCityAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    // Fetch data when user presses Enter
                    progressBar.setVisibility(View.VISIBLE);
                    new SearchCityAPI(SearchCityActivity.this, mainActivity.getAPI_Key(), query).execute();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    cities.clear();
                    searchCityAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    public void updateSearchResults(List<City> newCities) {
        progressBar.setVisibility(View.GONE);
        if (newCities.isEmpty()) {
            Toast.makeText(this, "No results found!", Toast.LENGTH_SHORT).show();
        }
        cities.clear();
        cities.addAll(newCities);
        searchCityAdapter.notifyDataSetChanged();
    }

}

