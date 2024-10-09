package com.example.myweatherapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddCityActivity extends AppCompatActivity {
    private TextView cancelAddCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_city);
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
        cancelAddCity.setOnClickListener(v -> onBackPressed());
    }
}

