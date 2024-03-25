package com.weather.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.models.Weather;
import com.weather.service.WeatherService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.SQLException;
import java.util.Map;

public class MainServlet extends jakarta.servlet.http.HttpServlet {

    public static String url = "https://api.weather.yandex.ru/v2/fact";
    public static String yandexWeatherAPI = "dbde8b79-ad25-45e9-85cc-cdb22af4756b";
    public static String httpGeocoderAPI = "44603ddf-f09b-468b-a529-17a8bf3cbdee";
    public static String lat = "";
    public static String lon = "";

    private final WeatherService weatherService;

    public MainServlet() {
        this.weatherService = new WeatherService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String city;
        HttpSession session = req.getSession();
        session.setAttribute("isAtt", false);

        if ((city = req.getParameter("city")) != null) {
            session.setAttribute("town", city);
            httpGeocoderRequest(city);
            StringBuilder result = httpYandexWeatherRequest();

            session.setAttribute("isAtt", true);
            session.setAttribute("weather", result);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(session.getAttribute("weather").toString(), Map.class);
            Weather weather = new Weather(city,
                    new Double(String.valueOf(map.get("temp").toString() == null ? 0 : map.get("temp").toString())),
                    new Double(String.valueOf(map.get("feels_like").toString() == null ? 0 : map.get("feels_like").toString())),
                    new Integer(String.valueOf(map.get("humidity").toString() == null ? 0 : map.get("humidity").toString())),
                    new Double(String.valueOf(map.get("wind_speed").toString() == null ? 0 : map.get("wind_speed").toString())),
                    new Double(String.valueOf(map.get("temp_water") == null ? 0 : map.get("temp_water").toString())));

            try {
                addToDataBase(weather);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("main.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    }

    private void httpGeocoderRequest(String city) {
        try {
            String encodedCityName = URLEncoder.encode(city, "UTF-8");
            URL url = new URL("https://geocode-maps.yandex.ru/1.x/?" +
                    "format=json" +
                    "&geocode=" + encodedCityName +
                    "&apikey=" + httpGeocoderAPI);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();

                response.append(reader.readLine());

                String[] coor = response.substring(response.indexOf("Point") + 15, response.indexOf("Point") + 31).split(" ");
                lat = coor[1];
                lon = coor[0];

                reader.close();

            } else {
                System.out.println("Ошибка при запросе к API: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder httpYandexWeatherRequest() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("X-Yandex-API-Key", yandexWeatherAPI);

        URI uri = null;
        try {
            uri = new URIBuilder(request.getURI())
                    .addParameter("lat", lat)
                    .addParameter("lon", lon)
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ((HttpRequestBase) request).setURI(uri);

        HttpResponse httpResponse = client.execute(request);

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result;
    }

    private void addToDataBase(Weather weather) throws SQLException {
        weatherService.save(weather);
    }
}











