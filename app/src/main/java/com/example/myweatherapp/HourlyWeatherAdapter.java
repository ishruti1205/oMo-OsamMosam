package com.example.myweatherapp;

import com.bumptech.glide.Glide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {

    private List<HourlyWeather> hourlyWeatherList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHour;
        public ImageView imageViewWeather;
        public TextView textViewTemp;

        public ViewHolder(View view) {
            super(view);
            textViewHour = view.findViewById(R.id.hourly_time);
            imageViewWeather = view.findViewById(R.id.hourly_weather_icon);
            textViewTemp = view.findViewById(R.id.current_temperature);
        }
    }

    public HourlyWeatherAdapter(List<HourlyWeather> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyWeather hourlyWeather = hourlyWeatherList.get(position);
        holder.textViewHour.setText(hourlyWeather.getHourly_time());
        holder.textViewTemp.setText(hourlyWeather.getHourly_temperature());

        if (hourlyWeather.getHourly_weather_icon_url() != null && !hourlyWeather.getHourly_weather_icon_url().isEmpty()) {
            // Load image from URL using Glide
            Glide.with(holder.itemView.getContext())
                    .load(hourlyWeather.getHourly_weather_icon_url())
                    .into(holder.imageViewWeather);
        } else {
            // Use drawable resource
            holder.imageViewWeather.setImageResource(hourlyWeather.getHourly_weather_icon());
        }
    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }
}
