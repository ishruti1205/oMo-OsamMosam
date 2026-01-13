package com.example.myweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchCityAdapter extends RecyclerView.Adapter<SearchCityAdapter.CityViewHolder> {
    private List<City> cityList;
    private OnCityClickListener listener;

    public SearchCityAdapter(List<City> cityList, OnCityClickListener listener) {
        this.cityList = cityList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city_search, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.cityName.setText(city.getName());
        holder.stateName.setText(city.getState() + ", ");
        holder.countryName.setText(city.getCountry());
        holder.itemView.setOnClickListener(v -> listener.onCityClick(city));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityName, stateName, countryName;

        public CityViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            stateName = itemView.findViewById(R.id.stateName);
            countryName = itemView.findViewById(R.id.countryName);
        }
    }

    public interface OnCityClickListener {
        void onCityClick(City city);
    }
}
