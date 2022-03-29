package com.sudoku.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMain extends Application {
    public static void main(String args[]) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("sudoku.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 600);
        SudokuController sudokuController = fxmlLoader.getController();
        sudokuController.setupEvents();

        stage.setTitle("Sudoku");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setScene(scene);
        stage.show();
    }
}