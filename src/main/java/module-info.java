module com.example.goodbreakout {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.goodbreakout to javafx.fxml;
    exports com.example.goodbreakout;
}