module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires jdk.httpserver;
    requires java.net.http;
    requires java.desktop;
    requires org.controlsfx.controls;

    opens org.example to javafx.fxml;
    opens org.example.about to javafx.fxml;
    opens org.example.brake to javafx.fxml;
    opens org.example.clutch to javafx.fxml;
    opens org.example.throttle to javafx.fxml;
    opens org.example.overlay to javafx.fxml;
    opens org.example.time to javafx.fxml;
    opens org.example.theme to javafx.fxml;
    opens org.example.calibrate to javafx.fxml;
    opens org.example.bulletgraph to javafx.fxml;


    exports org.example;
}