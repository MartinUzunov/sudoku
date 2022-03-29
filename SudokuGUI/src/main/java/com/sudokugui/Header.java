package com.sudokugui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

/**
 * The class is used to display information about the Difficulty level and the elapsed time of the game.
 */
public class Header extends HBox {
    private final Label time;
    private final Label difficulty;
    private final String username;
    private Timeline timeline;

    public Header() {
        // Entering the username
        TextInputDialog textInputDialog = new TextInputDialog("Username");
        textInputDialog.getDialogPane().lookupButton(ButtonType.CANCEL).setOnMousePressed(mouseEvent -> System.exit(0));
        textInputDialog.setGraphic(null);
        textInputDialog.setTitle("Input");
        textInputDialog.setHeaderText("Please enter username.");
        textInputDialog.showAndWait();

        username = textInputDialog.getEditor().getText();

        time = new Label("");
        time.setStyle("-fx-text-fill: gray; -fx-font-size: 20px");

        difficulty = new Label("Easy");
        difficulty.setStyle("-fx-text-fill: gray; -fx-font-size: 20px");

        setupTime();

        HBox left = new HBox();
        left.setAlignment(Pos.CENTER_LEFT);

        Label labelLeft = new Label("Difficulty: ");
        labelLeft.setStyle("-fx-font-size: 20px;");

        left.getChildren().add(labelLeft);
        left.getChildren().add(difficulty);

        HBox right = new HBox();
        right.setAlignment(Pos.CENTER_RIGHT);
        right.getChildren().add(time);

        HBox.setHgrow(right, Priority.ALWAYS);

        getChildren().add(left);
        getChildren().add(right);
    }

    public void changeDifficultyLabel(String newDifficulty) {
        Platform.runLater(() -> difficulty.setText(newDifficulty));
    }

    public void startTime() {
        timeline.play();
    }

    public void stopTime() {
        timeline.stop();
    }

    public void resetTime() {
        setupTime();
    }

    public String getUsername() {
        return username;
    }

    public String getDifficultyLabel() {
        return difficulty.getText();
    }

    public String getElapsedTime() {
        return time.getText();
    }

    private void setupTime() {
        final int[] seconds = {0};
        final int[] minutes = {0};
        final int[] hours = {0};

        // counts the seconds, the minutes and the hours.
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            seconds[0] += 1;
            if (seconds[0] == 60) {
                seconds[0] = 0;
                minutes[0] += 1;
                if (minutes[0] == 60) {
                    minutes[0] = 0;
                    hours[0] += 1;
                }
            }

            String secondsStr;
            String minutesStr;
            String hoursStr;

            if (seconds[0] / 10 == 0) {
                secondsStr = "0" + seconds[0];
            } else {
                secondsStr = String.valueOf(seconds[0]);
            }

            if (minutes[0] / 10 == 0) {
                minutesStr = "0" + minutes[0];
            } else {
                minutesStr = String.valueOf(minutes[0]);
            }

            if (hours[0] / 10 == 0) {
                hoursStr = "0" + hours[0];
            } else {
                hoursStr = String.valueOf(hours[0]);
            }

            time.setText(hoursStr + ":" + minutesStr + ":" + secondsStr);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }
}
