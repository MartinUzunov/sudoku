package com.sudokugui;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * Represents all Sudoku related controls - Create New Game, Toggle notes on/off, Undo move, Redo move, Erase cell,
 * Solve Sudoku and Numpad of all valid digits.
 */
public class ControlPanel extends VBox {
    private final int ICON_SIZE = 28;
    private final String TEXT_COLOR = "-fx-text-fill: #0065c8;";

    private final Callback callback;
    private final DifficultyMenu difficultyMenu;

    public ControlPanel(DifficultyMenu difficultyMenu, Callback callback) {
        this.callback = callback;
        this.difficultyMenu = difficultyMenu;
        setAlignment(Pos.CENTER);
        setSpacing(20);

        createNewGameButton();
        createControls();
        createNumpad();
    }

    private void createNewGameButton() {
        Button newGame = new Button("New Game");
        newGame.setPrefSize(320, 30);
        newGame.setMaxSize(320, 30);

        newGame.setOnMouseClicked(mouseEvent -> {
            Bounds boundsInScene = newGame.localToScene(newGame.getBoundsInLocal());
            difficultyMenu.show(newGame, boundsInScene.getCenterX() + newGame.getWidth() / 1.4,
                    boundsInScene.getCenterY() + newGame.getHeight() * 2);
        });

        newGame.setStyle("-fx-background-color: #0072e3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: " +
                "30px; -fx-background-radius: 4;");

        newGame.setOnMouseEntered(mouseEvent -> newGame.setStyle("-fx-background-color: #0065c8; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: " +
                "30px; -fx-background-radius: 4; -fx-cursor: hand;"));

        newGame.setOnMouseExited(mouseEvent -> newGame.setStyle("-fx-background-color: #0072e3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: " +
                "30px; -fx-background-radius: 4;"));

        newGame.addEventFilter(MouseEvent.ANY, mouseEvent -> {
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                newGame.setStyle("-fx-background-color: #3a90e5; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: " +
                        "30px; -fx-background-radius: 4;");
            } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                newGame.setStyle("-fx-background-color: #0065c8; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: " +
                        "30px; -fx-background-radius: 4;");
            }
        });

        getChildren().add(newGame);
    }

    private void createControls() {
        HBox hBox = new HBox(10);

        hBox.getChildren().addAll(createNotesControl(), createUndoControl(), createRedoControl(),
                createEraseControl(), createSolveControl());

        getChildren().add(hBox);
    }

    private StackPane createNotesControl() {
        StackPane notesStackPane = new StackPane();
        Circle notesStatus = new Circle(7);
        notesStatus.setFill(Color.DARKRED);

        VBox notesVBox = new VBox(1);
        notesVBox.setAlignment(Pos.CENTER);

        Circle notes = new Circle(ICON_SIZE);
        notes.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResourceAsStream("note.png")))));
        notes.setOnMousePressed(mouseEvent -> {
            callback.notesCall();
            if (notesStatus.getFill() == Color.GREEN) {
                notesStatus.setFill(Color.DARKRED);
            } else {
                notesStatus.setFill(Color.GREEN);
            }
        });
        bindCircleHoverEffect(notes);

        Label notesLabel = new Label("Notes");
        notesLabel.setStyle(TEXT_COLOR);

        notesVBox.getChildren().add(notes);
        notesVBox.getChildren().add(notesLabel);

        notesStackPane.getChildren().add(notesVBox);
        notesStackPane.getChildren().add(notesStatus);
        StackPane.setAlignment(notesStatus, Pos.TOP_RIGHT);

        return notesStackPane;
    }

    private VBox createUndoControl() {
        VBox undoVBox = new VBox(1);
        undoVBox.setAlignment(Pos.CENTER);

        Circle undo = new Circle(ICON_SIZE);
        undo.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResourceAsStream("undo.png")))));
        undo.setOnMousePressed(mouseEvent -> callback.undoCall());
        bindCircleHoverEffect(undo);

        Label undoLabel = new Label("Undo");
        undoLabel.setStyle(TEXT_COLOR);

        undoVBox.getChildren().add(undo);
        undoVBox.getChildren().add(undoLabel);

        return undoVBox;
    }

    private VBox createRedoControl() {
        VBox redoVBox = new VBox(1);
        redoVBox.setAlignment(Pos.CENTER);

        Circle redo = new Circle(ICON_SIZE);
        redo.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResourceAsStream("redo.png")))));
        redo.setOnMousePressed(mouseEvent -> callback.redoCall());
        bindCircleHoverEffect(redo);

        Label redoLabel = new Label("Redo");
        redoLabel.setStyle(TEXT_COLOR);

        redoVBox.getChildren().add(redo);
        redoVBox.getChildren().add(redoLabel);

        return redoVBox;
    }

    private VBox createEraseControl() {
        VBox eraseVBox = new VBox(1);
        eraseVBox.setAlignment(Pos.CENTER);

        Circle erase = new Circle(ICON_SIZE);
        erase.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResourceAsStream("erase.png")))));
        erase.setOnMousePressed(mouseEvent -> callback.eraseCall());
        bindCircleHoverEffect(erase);

        Label eraseLabel = new Label("Erase");
        eraseLabel.setStyle(TEXT_COLOR);

        eraseVBox.getChildren().add(erase);
        eraseVBox.getChildren().add(eraseLabel);

        return eraseVBox;
    }

    private VBox createSolveControl() {
        VBox solveVBox = new VBox(1);
        solveVBox.setAlignment(Pos.CENTER);

        Circle solve = new Circle(ICON_SIZE);
        solve.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResourceAsStream("solve.png")))));
        solve.setOnMousePressed(mouseEvent -> callback.solveCall());
        bindCircleHoverEffect(solve);

        Label solveLabel = new Label("Solve");
        solveLabel.setStyle(TEXT_COLOR);

        solveVBox.getChildren().add(solve);
        solveVBox.getChildren().add(solveLabel);

        return solveVBox;
    }

    private void createNumpad() {
        VBox numpadHolder = new VBox(3);
        HBox row = new HBox(3);

        for (int i = 1; i <= 9; i += 1) {
            Button button = new Button(String.valueOf(i));
            button.setMinWidth(120);
            button.setMinHeight(120);
            button.setStyle("-fx-background-color: #eaeef4; -fx-text-fill: #0072e3; -fx-font-size: " +
                    "55px; -fx-background-radius: 10;");
            bindNumpadHoverEffect(button);
            int finalI = i;
            button.setOnAction(actionEvent -> callback.numpadCall(finalI));

            row.getChildren().add(button);

            if (i == 3 || i == 6 || i == 9) {
                numpadHolder.getChildren().add(row);
                row = new HBox(3);
                row.setPrefWidth(300);
            }
        }

        getChildren().add(numpadHolder);
    }

    private void bindNumpadHoverEffect(Button button) {
        button.setOnMouseEntered(mouseEvent -> button.setStyle("-fx-background-color: #dce3ed; -fx-text-fill: #0072e3; -fx-font-size: " +
                "55px; -fx-background-radius: 10; -fx-cursor: hand;"));

        button.setOnMouseExited(mouseEvent -> button.setStyle("-fx-background-color: #eaeef4; -fx-text-fill: #0072e3; -fx-font-size: " +
                "55px; -fx-background-radius: 10;"));

        button.addEventFilter(MouseEvent.ANY, mouseEvent -> {
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                button.setStyle("-fx-background-color: #d2dae7; -fx-text-fill: #0072e3; -fx-font-size: " +
                        "55px; -fx-background-radius: 10;");
            } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                button.setStyle("-fx-background-color: #dce3ed; -fx-text-fill: #0072e3; -fx-font-size: " +
                        "55px; -fx-background-radius: 10;");
            }
        });
    }

    private void bindCircleHoverEffect(Circle circle) {
        circle.setOnMouseEntered(mouseEvent -> {
            circle.setBlendMode(BlendMode.HARD_LIGHT);
            circle.setStyle("-fx-cursor: hand;");
        });

        circle.setOnMouseExited(mouseEvent -> {
            circle.setBlendMode(null);
            circle.setStyle(null);
        });
    }
}
