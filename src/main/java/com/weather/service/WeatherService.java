package com.weather.service;

import com.weather.models.Weather;
import com.weather.repositories.WeatherRepository;
import com.weather.repositories.WeatherRepositoryImpl;

import java.sql.SQLException;
import java.util.Optional;

public class WeatherService {
    private final WeatherRepository weatherRepository;

    public WeatherService() {
        weatherRepository = new WeatherRepositoryImpl();
    }

    public Optional<Weather> findById(String city) throws SQLException {
        return weatherRepository.findById(city);
    }

    public void save(Weather weather) throws SQLException {
        weatherRepository.save(weather);
    }

    public void delete(String city) throws SQLException {
        weatherRepository.delete(city);
    }
}
