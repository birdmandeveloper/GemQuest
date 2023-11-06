module com.example.realdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens tech to javafx.fxml;
    exports tech;
}