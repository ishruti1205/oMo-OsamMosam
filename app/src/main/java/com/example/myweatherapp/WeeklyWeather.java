package com.example.myweatherapp;

public class WeeklyWeather {
    private String weekly_month;
    private String weekly_date;
    private String weekly_day;
    private String weekly_weather_icon_url;
    private int weekly_weather_icon;
    private String weekly_temperature_min;
    private String weekly_temperature_max;

    public WeeklyWeather(String weekly_month, String weekly_date, String weekly_day, String weekly_weather_icon_url, String weekly_temperature_min, String weekly_temperature_max) {
        this.weekly_month = weekly_month;
        this.weekly_date = weekly_date;
        this.weekly_day = weekly_day;
        this.weekly_weather_icon_url = weekly_weather_icon_url;
        this.weekly_temperature_min = weekly_temperature_min;
        this.weekly_temperature_max = weekly_temperature_max;
    }

    public WeeklyWeather(String weekly_month, String weekly_date, String weekly_day, int weekly_weather_icon, String weekly_temperature_min, String weekly_temperature_max) {
        this.weekly_month = weekly_month;
        this.weekly_date = weekly_date;
        this.weekly_day = weekly_day;
        this.weekly_weather_icon = weekly_weather_icon;
        this.weekly_temperature_min = weekly_temperature_min;
        this.weekly_temperature_max = weekly_temperature_max;
    }

    public String getWeekly_month() {
        return weekly_month;
    }

    public String getWeekly_date() {
        return weekly_date;
    }

    public String getWeekly_day() {
        return weekly_day;
    }

    public int getWeekly_weather_icon() {
        return weekly_weather_icon;
    }
    public String getWeekly_weather_icon_url() {
        return weekly_weather_icon_url;
    }

    public String getWeekly_temperature_max() {
        return weekly_temperature_max;
    }

    public String getWeekly_temperature_min() {
        return weekly_temperature_min;
    }
}
