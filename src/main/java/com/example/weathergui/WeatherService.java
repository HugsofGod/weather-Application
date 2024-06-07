package com.example.weathergui;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WeatherService {
    private static final String API_KEY = "0cdc8fe6e676b521614fb9cfb7ab4635"; // Replace with your actual Weatherstack API key
    private static final String API_URL = "http://api.weatherstack.com/current";
    private static final String FORECAST_API_URL = "http://api.weatherstack.com/forecast";

    public WeatherData getWeatherData(String location) throws Exception {
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8.toString());
        String urlString = API_URL + "?access_key=" + API_KEY + "&query=" + encodedLocation;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Failed to get current weather data: HTTP error code : " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        // Print the raw JSON response
        System.out.println("Current Weather JSON: " + content.toString());

        JSONObject json = new JSONObject(content.toString());
        if (json.has("current")) {
            JSONObject current = json.getJSONObject("current");

            WeatherData weatherData = new WeatherData();
            weatherData.setTemperature(current.optDouble("temperature", Double.NaN));
            weatherData.setHumidity(current.optInt("humidity", -1));
            weatherData.setWeatherDescription(current.getJSONArray("weather_descriptions").optString(0, "No description"));
            weatherData.setWeatherIconUrl(current.getJSONArray("weather_icons").optString(0, ""));
            return weatherData;
        } else {
            throw new Exception("No current weather data available");
        }
    }

    public WeatherData getForecastData(String location) throws Exception {
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8.toString());
        String urlString = FORECAST_API_URL + "?access_key=" + API_KEY + "&query=" + encodedLocation;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Failed to get forecast data: HTTP error code : " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        // Print the raw JSON response
        System.out.println("Forecast JSON: " + content.toString());

        JSONObject json = new JSONObject(content.toString());
        if (json.has("forecast")) {
            JSONObject forecast = json.getJSONObject("forecast");
            JSONObject forecastDay = forecast.keys().hasNext() ? forecast.getJSONObject(forecast.keys().next()) : null;

            if (forecastDay != null) {
                WeatherData weatherData = new WeatherData();
                weatherData.setTemperature(forecastDay.optDouble("avgtemp", Double.NaN));
                weatherData.setHumidity(forecastDay.optInt("avghumidity", -1));

                if (forecastDay.has("weather_descriptions")) {
                    weatherData.setWeatherDescription(forecastDay.getJSONArray("weather_descriptions").optString(0, "No description"));
                }

                if (forecastDay.has("weather_icons")) {
                    weatherData.setWeatherIconUrl(forecastDay.getJSONArray("weather_icons").optString(0, ""));
                }

                return weatherData;
            }
        }
        throw new Exception("No forecast data available");
    }
}
