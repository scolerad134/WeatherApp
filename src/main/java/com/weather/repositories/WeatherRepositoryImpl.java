package com.weather.repositories;

import com.weather.models.Weather;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.Optional;

public class WeatherRepositoryImpl implements WeatherRepository {

    public static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Weather.class)
                .buildSessionFactory();
    }

    @Override
    public Optional<Weather> findById(String city) throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        Optional<Weather> optionalWeather;

        session.beginTransaction();
        Weather weather = session.get(Weather.class, city);
        session.getTransaction().commit();

        if (weather == null) {
            optionalWeather = Optional.empty();
        } else {
            optionalWeather = Optional.of(weather);
        }

        return optionalWeather;
    }

    @Override
    public void save(Weather weather) throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(weather);
        session.getTransaction().commit();
    }

    @Override
    public void delete(String city) throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.remove(findById(city).get());
        session.getTransaction().commit();
    }
}
