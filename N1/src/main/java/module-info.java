module java {
    requires javafx.controls;
    requires javafx.fxml;

   opens controller to javafx.fxml;
    exports application;

    exports controller;
    exports model;
    exports service;
}