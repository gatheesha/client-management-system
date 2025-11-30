module com.gatarita.games.clientmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires atlantafx.base;
    requires java.sql;
    requires java.logging;

    opens com.gatarita.games.clientmanagementsystem to javafx.fxml;
    exports com.gatarita.games.clientmanagementsystem;
}