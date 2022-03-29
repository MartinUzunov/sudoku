package com.sudokugui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Represents a Menu which appears when 'New Game' button in Control Panel is pressed.
 */
public class DifficultyMenu extends ContextMenu {

    public DifficultyMenu(){
        setStyle("-fx-selection-bar: #f3f6fa;");
        MenuItem easy = new MenuItem("Easy");
        MenuItem normal = new MenuItem("Normal");
        MenuItem hard = new MenuItem("Hard");
        MenuItem expert = new MenuItem("Expert");

        addStyleToMenuItems(easy,normal,hard,expert);
        getItems().addAll(easy,normal,hard,expert);
    }

    private void addStyleToMenuItems(MenuItem ...items){
        for(MenuItem item : items){
            item.setStyle("-fx-font-size: 28px; -fx-text-fill: gray; -fx-font-weight: bold;");
        }
    }
}
