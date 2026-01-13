package com.example.myweatherapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private ArrayList<City> cities;
    private OnCityClickListener onCityClickListener;
    private boolean isDeleteMode = false;  // To track delete mode

    public CityAdapter(ArrayList<City> cities, OnCityClickListener onCityClickListener) {
        this.cities = cities;
        this.onCityClickListener = onCityClickListener;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        City city = cities.get(position);
        holder.cityName.setText(city.getName());
        holder.stateName.setText(city.getState() + ", ");
        holder.countryName.setText(city.getCountry());

        // Handle delete icon visibility and checkbox behavior
        if (isDeleteMode) {
            holder.deleteIcon.setVisibility(View.VISIBLE);
            holder.deleteIcon.setOnClickListener(v -> {
                showDeleteDialog(holder.itemView.getContext(), city, position);
            });
        } else {
            holder.deleteIcon.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> onCityClickListener.onCityClick(city));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        isDeleteMode = deleteMode;
        notifyDataSetChanged();
    }

    private void showDeleteDialog(Context context, City city, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete City")
                .setMessage("Are you sure you want to delete " + city.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    cities.remove(position);
                    notifyItemRemoved(position);
                    saveCities(context); // Pass context here
                    Toast.makeText(context, city.getName() + " deleted.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void saveCities(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CityPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String citiesJson = new Gson().toJson(cities);
        editor.putString("addedCities", citiesJson);
        editor.apply();
    }

    public interface OnCityClickListener {
        void onCityClick(City city);
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        TextView cityName, stateName, countryName;
        ImageView deleteIcon;

        public CityViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            stateName = itemView.findViewById(R.id.stateName);
            countryName = itemView.findViewById(R.id.countryName);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }

}
