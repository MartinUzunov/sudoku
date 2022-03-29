package com.sudoku.client;

import com.sudoku.ServerInterface;
import com.sudokugui.*;
import com.sudoku.server.GenerateSudoku;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.rmi.Naming;
import java.util.ResourceBundle;

public class SudokuController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private VBox vBox;

    @FXML
    private HBox hBox;

    private Header header;
    private Grid grid;
    private ControlPanel controlPanel;

    private ServerInterface serverObject;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createHeader();
        createGrid();
        createControlPanel(createDifficultyMenu());

        vBox.getChildren().add(0, header);
        hBox.getChildren().add(grid);
        hBox.getChildren().add(controlPanel);
    }

    public void setupEvents() {
        vBox.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            grid.handleKeys(event.getCode());
            event.consume();
        });
    }

    private void createHeader() {
        header = new Header();
        header.changeDifficultyLabel("Normal");
        header.setMinWidth(900);
        header.setPadding(new Insets(0, 30, 0, 50));
        header.startTime();
    }

    private void createGrid() {
        grid = new Grid(header, new Runnable() {
            @Override
            public void run() {
                String line =
                        "Username: " + header.getUsername() + ", Time: " + header.getElapsedTime() + ", " +
                                "Difficulty: " + header.getDifficultyLabel() + ", Solved";
                try {
                    serverObject.writeToFile(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        grid.setAlignment(Pos.CENTER_LEFT);

        int sudokuGrid[][] = new int[0][];

        try {
            serverObject = (ServerInterface) Naming.lookup("rmi://localhost:4999" + "/sudoku");
            sudokuGrid = serverObject.generateSudoku(GenerateSudoku.Difficulty.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        grid.loadGrid(sudokuGrid);
    }

    private DifficultyMenu createDifficultyMenu() {

        final Task[] loadNewSudoku = {null};

        final VBox[] root = {null};

        DifficultyMenu difficultyMenu = new DifficultyMenu();
        // set the action for every menu item
        for (MenuItem item : difficultyMenu.getItems()) {
            item.setOnAction(actionEvent -> {
                loadNewSudoku[0] = new Task() {
                    @Override
                    protected Object call() {
                        GenerateSudoku.Difficulty difficulty = null;

                        switch (item.getText()) {
                            case "Easy":
                                difficulty = GenerateSudoku.Difficulty.EASY;
                                header.changeDifficultyLabel(
                                        "Easy");
                                break;
                            case "Normal":
                                difficulty = GenerateSudoku.Difficulty.NORMAL;
                                header.changeDifficultyLabel(
                                        "Normal");
                                break;
                            case "Hard":
                                difficulty = GenerateSudoku.Difficulty.HARD;
                                header.changeDifficultyLabel(
                                        "Hard");
                                break;
                            case "Expert":
                                difficulty = GenerateSudoku.Difficulty.EXPERT;
                                header.changeDifficultyLabel(
                                        "Expert");
                                break;
                        }

                        // send information to the server if new Sudoku is requested and the current one is not solved
                        if (!grid.isSolved()) {
                            String line =
                                    "Username: " + header.getUsername() + ", Time: " + header.getElapsedTime() + ", " +
                                            "Difficulty: " + header.getDifficultyLabel() + ", Not Solved";
                            try {
                                serverObject.writeToFile(line);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            int newSudokuGrid[][] = serverObject.generateSudoku(difficulty);
                            Platform.runLater(() -> {
                                grid.loadGrid(newSudokuGrid);
                                grid.removeAllBackgrounds(true);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        header.stopTime();
                        header.resetTime();
                        header.startTime();
                        return null;
                    }
                };

                ProgressBar pBar = new ProgressBar();
                pBar.progressProperty().bind(loadNewSudoku[0].progressProperty());
                Label statusLabel = new Label("Loading");
                statusLabel.textProperty().bind(loadNewSudoku[0].messageProperty());
                root[0] = new VBox(5, statusLabel, pBar);
                root[0].setAlignment(Pos.CENTER);

                stackPane.getChildren().add(root[0]);

                new Thread(loadNewSudoku[0]).start();

                loadNewSudoku[0].setOnSucceeded(e -> stackPane.getChildren().remove(1));
            });
        }
        return difficultyMenu;
    }

    private void createControlPanel(DifficultyMenu difficultyMenu) {
        controlPanel = new ControlPanel(difficultyMenu, new Callback() {

            @Override
            public void numpadCall(int num) {
                grid.handleNumpad(num);
            }

            @Override
            public void notesCall() {
                grid.toggleNotes();
            }

            @Override
            public void eraseCall() {
                grid.handleErase();
            }

            @Override
            public void undoCall() {
                grid.handleUndo();
            }

            @Override
            public void redoCall() {
                grid.handleRedo();
            }

            @Override
            public void solveCall() {
                if(!grid.isSolved()) {
                    String line =
                            "Username: " + header.getUsername() + ", Time: " + header.getElapsedTime() + ", " +
                                    "Difficulty: " + header.getDifficultyLabel() + ", Used Solver";
                    try {
                        serverObject.writeToFile(line);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                int board[][] = grid.getInitialGrid();
                try {
                    board = serverObject.solveSudoku(board);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                grid.removeAllBackgrounds(true);
                grid.loadGrid(board);
            }

        });
    }
}
