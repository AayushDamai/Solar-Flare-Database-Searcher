module com.example.donkisolarflares {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.donkisolarflares to javafx.fxml;
    exports com.example.donkisolarflares;
}