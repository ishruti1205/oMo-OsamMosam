package com.example.myweatherapp;

import java.io.Serializable;

public class City implements Serializable {
    private String name;
    private String state;
    private String country;
    private double latitude;
    private double longitude;

    public City(String name, String state, String country, double latitude, double longitude) {
        this.name = name;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public String getState() { return state; }
    public String getCountry() { return country; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
