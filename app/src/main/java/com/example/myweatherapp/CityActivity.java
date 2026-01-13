package com.example.myweatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {
    private final MainActivity mainActivity = new MainActivity();
    MaterialButton addCityBtn;
    ImageView manage_cities_img, backBtn;
    private RecyclerView recyclerViewAddedCities;
    private CityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_city);

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft() + systemBars.left,
                    50 + systemBars.top,
                    v.getPaddingRight() + systemBars.right,
                    v.getPaddingBottom() + systemBars.bottom
            );
            return insets;
        });

        // Initialize views
        addCityBtn = findViewById(R.id.addCityBtn);
        manage_cities_img = findViewById(R.id.manage_cities_img);
        backBtn = findViewById(R.id.backBtn);

        recyclerViewAddedCities = findViewById(R.id.recyclerViewAddedCities);
        recyclerViewAddedCities.setLayoutManager(new LinearLayoutManager(this));

        loadCities(); // Load cities into the RecyclerView

        // Back button click listener
        backBtn.setOnClickListener(v -> finish());

        // Add city button click listener
        addCityBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CityActivity.this, SearchCityActivity.class);
            startActivity(intent); // Start SearchCityActivity
        });

        // Manage cities button click listener
        manage_cities_img.setOnClickListener(v -> {
            // Toggle delete mode
            boolean isDeleteMode = cityAdapter != null && !cityAdapter.isDeleteMode();
            cityAdapter.setDeleteMode(isDeleteMode);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCities(); // Reload cities when returning from SearchCityActivity
    }

    private void loadCities() {
        SharedPreferences sharedPreferences = getSharedPreferences("CityPrefs", MODE_PRIVATE);
        String addedCitiesJson = sharedPreferences.getString("addedCities", "[]");
        Type type = new TypeToken<ArrayList<City>>() {}.getType();
        ArrayList<City> addedCities = new Gson().fromJson(addedCitiesJson, type);

        cityAdapter = new CityAdapter(addedCities, city -> {
            Toast.makeText(this, "Current City: " + city.getName(), Toast.LENGTH_SHORT).show();

            // Save the selected city to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lastSelectedCity", new Gson().toJson(city));
            editor.apply();

            Intent i = new Intent(CityActivity.this, MainActivity.class);
            i.putExtra("lat", city.getLatitude());
            i.putExtra("lon", city.getLongitude());
            startActivity(i);
        });
        recyclerViewAddedCities.setAdapter(cityAdapter);
    }
}