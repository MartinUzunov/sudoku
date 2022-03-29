package com.sudokugui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The class represents a single cell on the sudoku board.
 */
public class Cell extends StackPane {
    private final Color USER_NUMBER_COLOR = Color.web("#0072E3");
    private final Color HARDCODED_NUMBER_COLOR = Color.BLACK;
    private final String INNER_BORDER_COLOR = "gray";
    private final String OUTER_BORDER_COLOR = "black";

    private final Label mainNumber;
    private Label notes[];
    private int sector;
    private boolean isHardcoded;

    public Cell(int row, int column) {
        this.isHardcoded = false;
        this.sector = 3 * (row /3) + (column / 3);
        this.mainNumber = new Label("");
        this.mainNumber.setFont(new Font(40));
        this.mainNumber.setTextFill(USER_NUMBER_COLOR);

        createBorders(row, column);
        createNotes();

        getChildren().add(mainNumber);
    }

    public Cell(Cell other){
        mainNumber = new Label("");
        createNotes();
        updateCell(other);
    }

    public void enterNumber(String s, boolean isNote) {
        if(isHardcoded){
            return;
        }

        if (isNote) {
            mainNumber.setText("");
            toggleSingleNote(Integer.parseInt(s));
        } else {
            eraseNotes();

            // toggle
            if(mainNumber.getText().equals(s)) {
                mainNumber.setText("");
            }
            else {
                mainNumber.setText(s);
            }
        }
    }

    /**
     * Makes the cell unchangeable on User input
     *
     * @param num
     */
    public void hardcodeCell(int num){
        if(num == 0){
            isHardcoded = false;
        }
        else {
            mainNumber.setText(String.valueOf(num));
            mainNumber.setTextFill(HARDCODED_NUMBER_COLOR);
            isHardcoded = true;
        }
    }

    /**
     * Imports all the relevant information of the Cell parameter to the current Cell.
     *
     * @param cell
     */
    public void updateCell(Cell cell){
        if(isHardcoded){
            return;
        }

        mainNumber.setText(cell.mainNumber.getText());
        mainNumber.setVisible(cell.mainNumber.isVisible());
        sector = cell.sector;
        isHardcoded = cell.isHardcoded;

        setBackground(cell.getBackground());

        for(int i=0;i<9;i+=1){
            notes[i].setVisible(cell.notes[i].isVisible());
        }
    }

    public void eraseNote(int num){
        notes[num - 1].setVisible(false);
    }

    public void eraseNotes() {
        for (int i = 0; i < 9; i += 1) {
            notes[i].setVisible(false);
        }
    }

    public void clearCell(){
        eraseNotes();
        mainNumber.setText("");
        isHardcoded = false;
    }

    public void addBackground(String color) {
        if(color == null){
            setBackground(null);
            return;
        }
        setBackground(new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY,
                new Insets(0, 0, 0, 0))));
    }

    public int getMainNumber() {
        if (mainNumber.getText().equals("")) {
            return 0;
        }
        return Integer.parseInt(mainNumber.getText());
    }

    public int getSector(){
        return sector;
    }

    public boolean getIsHardcoded(){
        return isHardcoded;
    }

    private void toggleSingleNote(int note) {
        notes[note - 1].setVisible(!notes[note - 1].visibleProperty().get());
    }

    /**
     * Adds border to the cell, so it is displayed correctly in the full grid.
     *
     * @param row
     * @param column
     */
    private void createBorders(int row, int column) {
        double upperBorder = 0.4;
        double rightBorder = 0.5;
        double bottomBorder = 0.4;
        double leftBorder = 0.5;
        String upperColor = INNER_BORDER_COLOR;
        String rightColor = INNER_BORDER_COLOR;
        String bottomColor = INNER_BORDER_COLOR;
        String leftColor = INNER_BORDER_COLOR;

        if (row == 0 || row == 3 || row == 6) {
            upperBorder = 3;
            upperColor = OUTER_BORDER_COLOR;
        } else if (row == 8) {
            bottomBorder = 3;
            bottomColor = OUTER_BORDER_COLOR;
        }

        if (column == 0 || column == 3 || column == 6) {
            leftBorder = 3;
            leftColor = OUTER_BORDER_COLOR;
        } else if (column == 8) {
            rightBorder = 3;
            rightColor = OUTER_BORDER_COLOR;
        }

        setStyle("-fx-border-width: " + upperBorder + " " + rightBorder + " " + bottomBorder + " " + leftBorder + "; " +
                "-fx-border-color: " + upperColor + " " + rightColor + " " + bottomColor + " " + leftColor + ";");
    }

    /**
     * Initializes and styles the Labels[1-9] for the notes.
     */
    private void createNotes() {
        VBox notesHolder = new VBox(1);
        notesHolder.setPadding(new Insets(4, 5, 0, 5));
        notesHolder.setMinSize(50, 50);

        notes = new Label[9];
        HBox row = new HBox(13);
        for (int i = 0; i < 9; i += 1) {
            Label note = new Label(String.valueOf(i + 1));
            note.setTextFill(Color.GRAY);
            notes[i] = note;
            row.getChildren().add(notes[i]);
            if (i == 2 || i == 5 || i == 8) {
                notesHolder.getChildren().add(row);
                row = new HBox(13);
            }
        }

        getChildren().add(notesHolder);
        eraseNotes();
    }
}


