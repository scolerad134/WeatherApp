package com.weather.models;

public class Weather {
    private String city;
    private double temp;
    private double feels_like;
    private int humidity;
    private double wind_speed;
    private double temp_water;

    public Weather() {
    }

    public Weather(String city, double temp, double feels_like, int humidity, double wind_speed, double temp_water) {
        this.city = city;
        this.temp = temp;
        this.feels_like = feels_like;
        this.humidity = humidity;
        this.wind_speed = wind_speed;
        this.temp_water = temp_water;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public double getTemp_water() {
        return temp_water;
    }

    public void setTemp_water(double temp_water) {
        this.temp_water = temp_water;
    }
}
