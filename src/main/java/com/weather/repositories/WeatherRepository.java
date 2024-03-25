package com.weather.repositories;

import com.weather.models.Weather;

import java.sql.SQLException;
import java.util.Optional;

public interface WeatherRepository {
    Optional<Weather> findById(String city) throws SQLException;
    void save(Weather weather) throws SQLException;
    void delete(String city) throws SQLException;
}
