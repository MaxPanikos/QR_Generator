module com.example.qr_generator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires qrgen;

    opens com.example.qr_generator to javafx.fxml;
    exports com.example.qr_generator;
}