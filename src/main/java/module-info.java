module com.example.weathergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.weathergui to javafx.fxml;
    exports com.example.weathergui;
}