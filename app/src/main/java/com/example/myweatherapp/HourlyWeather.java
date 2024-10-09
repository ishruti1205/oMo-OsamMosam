package com.example.myweatherapp;

public class HourlyWeather {
    private String hourly_time;
    private int hourly_weather_icon;
    private String hourly_weather_icon_url;
    private String hourly_temperature;

    public HourlyWeather(String time, String  weatherIconURL, String hourly_temperature) {
        this.hourly_time = time;
        this.hourly_weather_icon_url = weatherIconURL;
        this.hourly_temperature = hourly_temperature;
    }

    public HourlyWeather(String time, int  weatherIcon, String hourly_temperature) {
        this.hourly_time = time;
        this.hourly_weather_icon = weatherIcon;
        this.hourly_temperature = hourly_temperature;
    }


    public String getHourly_time() { return hourly_time; }
    public String getHourly_temperature() { return hourly_temperature; }
    public String getHourly_weather_icon_url() { return hourly_weather_icon_url; }
    public int getHourly_weather_icon() { return hourly_weather_icon; }
}
