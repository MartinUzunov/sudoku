module com.sudokugui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.sudokugui to javafx.fxml;
    exports com.sudokugui;
}