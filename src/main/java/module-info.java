module com.example.projekt2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projekt2 to javafx.fxml;
    exports com.example.projekt2;
}