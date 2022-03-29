module com.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires com.sudokugui;


    opens com.sudoku.client to javafx.fxml;
    exports com.sudoku;
    exports com.sudoku.client;
}