package com.weather.repositories;

import com.weather.models.Weather;

import java.sql.*;
import java.util.Optional;

public class WeatherRepositoryImpl implements WeatherRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/lara";
    private static final String USERNAME = "dmosk";
    private static final String PASSWORD = "myPassword";
    private Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Weather> findById(String city) throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "select * from weather where city = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, city);
        ResultSet resultSet = stmt.executeQuery();
        Optional<Weather> optionalWeather;

        if (resultSet.next()) {
            Weather weather = new Weather(resultSet.getString("city"), resultSet.getDouble("temp"),
                    resultSet.getDouble("feels_like"), resultSet.getInt("humidity"),
                    resultSet.getDouble("wind_speed"), resultSet.getDouble("temp_water"));

            optionalWeather = Optional.of(weather);
        } else {
            optionalWeather = Optional.empty();
        }

        resultSet.close();
        stmt.close();
        connection.close();

        return optionalWeather;
    }

    @Override
    public void save(Weather weather) throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "insert into weather (city, temp, feels_like, humidity, wind_speed, temp_water) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, weather.getCity());
        stmt.setDouble(2, weather.getTemp());
        stmt.setDouble(3, weather.getFeels_like());
        stmt.setInt(4, weather.getHumidity());
        stmt.setDouble(5, weather.getWind_speed());
        stmt.setDouble(6, weather.getTemp_water());
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    @Override
    public void update(Weather weather) throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "update weather set temp = ?, feels_like = ?, humidity = ?, wind_speed = ?, temp_water = ? where city = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setDouble(1, weather.getTemp());
        stmt.setDouble(2, weather.getFeels_like());
        stmt.setInt(3, weather.getHumidity());
        stmt.setDouble(4, weather.getWind_speed());
        stmt.setDouble(5, weather.getTemp_water());
        stmt.setString(6, weather.getCity());
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    @Override
    public void delete(String city) throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "delete from weather where city = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, city);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }
}
