module com.gatarita.games.clientmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires atlantafx.base;

    opens com.gatarita.games.clientmanagementsystem to javafx.fxml;
    exports com.gatarita.games.clientmanagementsystem;
}