package com.example.weathergui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherApp extends Application {
    private WeatherService weatherService;
    private List<String> searchHistory = new ArrayList<>();
    private boolean isCelsius = true;
    private boolean isKmH = true;

    @Override
    public void start(Stage primaryStage) {
        weatherService = new WeatherService();

        // Create GUI components
        Label locationLabel = new Label("Enter location:");
        TextField locationField = new TextField();
        Button getWeatherButton = new Button("Get Weather");
        Label weatherInfoLabel = new Label();
        VBox forecastBox = new VBox(10); // Container for forecast information

        // Create an ImageView to display weather icon
        ImageView weatherIcon = new ImageView();
        weatherIcon.setFitWidth(50);
        weatherIcon.setFitHeight(50);

        // Unit conversion buttons
        ToggleButton tempUnitButton = new ToggleButton("°C/°F");
        ToggleButton windSpeedUnitButton = new ToggleButton("km/h/mph");

        // Search history ListView
        ListView<String> historyListView = new ListView<>();
        historyListView.setPrefHeight(100);

        // Set button action
        getWeatherButton.setOnAction(e -> {
            String location = locationField.getText();
            try {
                WeatherData currentWeather = weatherService.getWeatherData(location);
                displayCurrentWeather(currentWeather, weatherInfoLabel, weatherIcon);

                // Fetch and display forecast data
                WeatherData forecastWeather = weatherService.getForecastData(location);
                displayForecastWeather(forecastWeather, forecastBox);

                // Add to history
                addToHistory(location, historyListView);
            } catch (Exception ex) {
                weatherInfoLabel.setText("Error: " + ex.getMessage());
            }
        });

        tempUnitButton.setOnAction(e -> {
            isCelsius = !isCelsius;
            updateDisplayedWeather(weatherInfoLabel, weatherIcon);
        });

        windSpeedUnitButton.setOnAction(e -> {
            isKmH = !isKmH;
            updateDisplayedWeather(weatherInfoLabel, weatherIcon);
        });

        // Arrange components in a layout
        VBox contentPane = new VBox(10);
        contentPane.setPadding(new Insets(10));
        contentPane.getChildren().addAll(locationLabel, locationField, getWeatherButton, tempUnitButton, windSpeedUnitButton, weatherInfoLabel, weatherIcon, forecastBox, historyListView);

        // Set up the background dynamically based on time of day
        Region background = new Region();
        setDynamicBackground(background);

        // StackPane to layer background and content
        StackPane root = new StackPane();
        root.getChildren().addAll(background, contentPane);

        // Set up the scene
        Scene scene = new Scene(root, 400, 600);
        primaryStage.setTitle("Weather Information App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to set dynamic background based on time of day
    private void setDynamicBackground(Region background) {
        String timeOfDay = getTimeOfDay();
        String backgroundImageUrl = "/images/" + timeOfDay + ".jpg";
        InputStream backgroundImageStream = getClass().getResourceAsStream(backgroundImageUrl);
        if (backgroundImageStream != null) {
            BackgroundImage backgroundImage = new BackgroundImage(new Image(backgroundImageStream),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            background.setBackground(new Background(backgroundImage));
        } else {
            System.out.println("Background image not found for: " + timeOfDay);
        }
    }

    private String getTimeOfDay() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 12) {
            return "morning";
        } else if (hour >= 12 && hour < 18) {
            return "afternoon";
        } else if (hour >= 18 && hour < 21) {
            return "evening";
        } else {
            return "night";
        }
    }

    // Method to display current weather information including icons
    private void displayCurrentWeather(WeatherData weatherData, Label weatherInfoLabel, ImageView weatherIcon) {
        double temp = weatherData.getTemperature();
        int humidity = weatherData.getHumidity();
        String description = weatherData.getWeatherDescription();

        String weatherText = String.format("Temperature: %.1f%s\nHumidity: %d%%\nConditions: %s",
                isCelsius ? temp : temp * 9 / 5 + 32,
                isCelsius ? "°C" : "°F",
                humidity,
                description);

        weatherInfoLabel.setText(weatherText);

        // Display weather icon
        String iconUrl = weatherData.getWeatherIconUrl();
        if (iconUrl != null && !iconUrl.isEmpty()) {
            Image image = new Image(iconUrl);
            weatherIcon.setImage(image);
        }
    }

    // Method to display forecast weather information
    private void displayForecastWeather(WeatherData forecastWeather, VBox forecastBox) {
        forecastBox.getChildren().clear();
        Label forecastLabel = new Label("Forecast:");
        Label forecastInfoLabel = new Label("Temperature: " + (isCelsius ? forecastWeather.getTemperature() : forecastWeather.getTemperature() * 9 / 5 + 32) + (isCelsius ? "°C" : "°F") + "\n" +
                "Humidity: " + forecastWeather.getHumidity() + "%\n" +
                "Conditions: " + forecastWeather.getWeatherDescription());

        // Display forecast weather icon
        ImageView forecastIcon = new ImageView();
        forecastIcon.setFitWidth(50);
        forecastIcon.setFitHeight(50);
        String forecastIconUrl = forecastWeather.getWeatherIconUrl();
        if (forecastIconUrl != null && !forecastIconUrl.isEmpty()) {
            Image forecastImage = new Image(forecastIconUrl);
            forecastIcon.setImage(forecastImage);
        }

        forecastBox.getChildren().addAll(forecastLabel, forecastInfoLabel, forecastIcon);
    }

    private void updateDisplayedWeather(Label weatherInfoLabel, ImageView weatherIcon) {
        // Re-fetch the data and update the display based on the new unit settings
        String location = weatherInfoLabel.getText().split("\n")[0].split(":")[1].trim();
        try {
            WeatherData currentWeather = weatherService.getWeatherData(location);
            displayCurrentWeather(currentWeather, weatherInfoLabel, weatherIcon);
        } catch (Exception ex) {
            weatherInfoLabel.setText("Error: " + ex.getMessage());
        }
    }

    private void addToHistory(String location, ListView<String> historyListView) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String historyEntry = "Location: " + location + " at " + timestamp;
        searchHistory.add(historyEntry);
        historyListView.getItems().add(historyEntry);
    }

    public static void main(String[] args
    ) {
        launch(args);
    }
}
