README: Weather Information App
________________________________________
Overview
The Weather Information App is a Java-based application that provides real-time weather information for any given location. It utilizes the WeatherStack API to fetch weather data and displays the results in a user-friendly graphical user interface (GUI) built using JavaFX.
________________________________________
Features
•	Real-time Weather Data: Fetches and displays current temperature, humidity, and weather conditions.
•	Dynamic Backgrounds: Changes the background image based on the time of day.
•	Weather Icons: Displays an appropriate weather icon for the current conditions.
•	User-friendly GUI: Easy-to-use interface for entering locations and viewing weather information.
•	Error Handling: Provides meaningful error messages for invalid inputs or failed API requests.
•	History Tracking: Maintains a history of recent weather searches for quick reference.
________________________________________
Implementation Details
The Weather Information App is implemented using three main classes:
1.	WeatherService Class
o	Responsible for making HTTP requests to the WeatherStack API.
o	Parses the JSON response and creates WeatherData objects.
o	Handles URL encoding and connection management.
java
Copy code
public class WeatherService {
    // API Key and URL
    private static final String API_KEY = "your_api_key";
    private static final String API_URL = "http://api.weatherstack.com/current";

    // Method to get weather data
    public WeatherData getWeatherData(String location) throws Exception {
        // Implementation here...
    }
}
2.	WeatherApp Class
o	The main class that sets up the JavaFX application.
o	Contains the start method to initialize the primary stage and scene.
o	Handles user interactions and updates the GUI with weather data.
java
Copy code
public class WeatherApp extends Application {
    private WeatherService weatherService;
    private Label weatherInfoLabel;
    private ImageView weatherIcon;

    @Override
    public void start(Stage primaryStage) {
        // Implementation here...
    }

    private void displayCurrentWeather(WeatherData weatherData) {
        // Implementation here...
    }
}
3.	WeatherData Class
o	A model class that stores weather-related information.
o	Includes fields for temperature, humidity, weather description, and weather icon URL.
o	Provides getters and setters for encapsulation.
java
Copy code
public class WeatherData {
    private double temperature;
    private int humidity;
    private String weatherDescription;
    private String weatherIconUrl;

    // Getters and setters here...
}
________________________________________
Prerequisites
•	Java Development Kit (JDK): Ensure you have JDK 8 or higher installed.
•	JavaFX: Make sure JavaFX is set up in your development environment.
•	WeatherStack API Key: Obtain an API key from WeatherStack by signing up for a free account.
________________________________________
Setup and Usage
1.	Clone the Repository
o	Clone the repository to your local machine using:
bash
Copy code
git clone https://github.com/yourusername/weather-information-app.git
2.	Add the API Key
o	Open the WeatherService class.
o	Replace "your_api_key" with your actual WeatherStack API key.
3.	Compile and Run the Application
o	Navigate to the project directory.
o	Compile the Java files using:
bash
Copy code
javac -cp .:path/to/javafx-sdk/lib/* WeatherApp.java
o	Run the application using:
bash
Copy code
java -cp .:path/to/javafx-sdk/lib/* WeatherApp
4.	Using the Application
o	Enter a location in the text field and click the "Get Weather" button.
o	The current weather data for the entered location will be displayed.
________________________________________
Advanced Features
•	Dynamic Backgrounds:
o	The application changes the background image based on the time of day (morning, afternoon, evening, night).
o	Background images should be placed in the /images/ directory with names corresponding to the time of day (e.g., morning.jpg, afternoon.jpg).
•	Unit Conversion:
o	The application allows for temperature conversion between Celsius and Fahrenheit.
o	Implement this feature by adding a toggle button or a dropdown menu to the GUI.
________________________________________
References
•	Eck, D. J. (2019). Introduction to Programming Using Java. [URL]
For further details on GUI programming, refer to chapters 6 and 13 of Introduction to Programming Using Java by David J. Eck.
________________________________________
License
This project is licensed under the MIT License. See the LICENSE file for more details.
________________________________________


