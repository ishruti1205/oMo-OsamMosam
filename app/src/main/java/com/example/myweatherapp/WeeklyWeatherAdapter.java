package com.example.myweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WeeklyWeatherAdapter extends RecyclerView.Adapter<WeeklyWeatherAdapter.ViewHolder> {

    private List<WeeklyWeather> weatherList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView weeklyMonth, weeklyDate, weeklyDay, weeklyTemperatureMin, weeklyTemperatureMax;
        ImageView weeklyWeatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weeklyMonth = itemView.findViewById(R.id.weekly_month);
            weeklyDate = itemView.findViewById(R.id.weekly_date);
            weeklyDay = itemView.findViewById(R.id.weekly_day);
            weeklyWeatherIcon = itemView.findViewById(R.id.weekly_weather_icon);
            weeklyTemperatureMin = itemView.findViewById(R.id.weekly_temperature_min);
            weeklyTemperatureMax = itemView.findViewById(R.id.weekly_temperature_max);
        }
    }

    public WeeklyWeatherAdapter(List<WeeklyWeather> weatherList) {
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weekly_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeeklyWeather weeklyWeather = weatherList.get(position);

        holder.weeklyMonth.setText(weeklyWeather.getWeekly_month());
        holder.weeklyDate.setText(weeklyWeather.getWeekly_date());
        holder.weeklyDay.setText(weeklyWeather.getWeekly_day());

        // Load image from URL using Glide or set drawable resource
        if (weeklyWeather.getWeekly_weather_icon_url() != null && !weeklyWeather.getWeekly_weather_icon_url().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(weeklyWeather.getWeekly_weather_icon_url())
                    .into(holder.weeklyWeatherIcon);
        } else {
            holder.weeklyWeatherIcon.setImageResource(weeklyWeather.getWeekly_weather_icon());
        }

        holder.weeklyTemperatureMin.setText(weeklyWeather.getWeekly_temperature_min());
        holder.weeklyTemperatureMax.setText(weeklyWeather.getWeekly_temperature_max());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


}
