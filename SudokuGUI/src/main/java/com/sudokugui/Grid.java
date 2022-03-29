package com.sudokugui;

import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the full Sudoku board. Responsible for moving the current cell, for coloring the related cells and for
 * handling all Key and Control Panel events. Keeps track of the board changes using the History class.
 */
public class Grid extends VBox {
    private final String ROW_COLUMN_SECTOR_COLOR = "#E2EBF3";
    private final String SAME_NUMBER_COLOR = "#C3D7EA";
    private final String CURRENT_CELL_COLOR = "#BBDEFB";
    private final String INVALID_CELL_COLOR = "#F7CFD6";
    private final int GRID_SIZE = 9;

    private final Cell[][] cells;
    private int currentRow;
    private int currentColumn;
    private boolean notesOn;
    private final History history;
    private final Header header;
    private final Runnable solvedServerCallback;

    /**
     * @param header - the node which represents the Header element.
     * @param solved - callback function which performs the action that should be performed when the Sudoku is solved.
     */
    public Grid(Header header, Runnable solved) {
        this.cells = new Cell[GRID_SIZE][GRID_SIZE];
        this.currentRow = -1;
        this.currentColumn = -1;
        this.notesOn = true;
        this.history = new History();
        this.header = header;
        this.solvedServerCallback = solved;

        for (int i = 0; i < GRID_SIZE; i += 1) {
            HBox hBox = new HBox();
            for (int j = 0; j < GRID_SIZE; j += 1) {
                Cell cell = new Cell(i, j);
                setMouseClickEvent(cell, i, j);
                cells[i][j] = cell;
                hBox.getChildren().add(cells[i][j]);
            }
            getChildren().add(hBox);
        }
    }

    /**
     * Loads a grid and makes all non-empty cells hardcoded(unchangeable by user input).
     *
     * @param arr
     */
    public void loadGrid(int arr[][]){
        for(int i = 0; i< GRID_SIZE; i+=1){
            for(int j = 0; j< GRID_SIZE; j+=1){
                cells[i][j].clearCell();
                if(arr[i][j] >= 0 && arr[i][j] <=9) {
                    cells[i][j].hardcodeCell(arr[i][j]);
                }
            }
        }
    }

    public void toggleNotes() {
        notesOn = !notesOn;
    }

    public void handleKeys(KeyCode keyCode) {
        if (keyCode == KeyCode.UP) {
            if (currentRow - 1 >= 0) {
                currentRow -= 1;
                moveCell();
            }
        } else if (keyCode == KeyCode.RIGHT) {
            if (currentColumn + 1 < GRID_SIZE) {
                currentColumn += 1;
                moveCell();
            }
        } else if (keyCode == KeyCode.DOWN) {
            if (currentRow + 1 < GRID_SIZE) {
                currentRow += 1;
                moveCell();
            }
        } else if (keyCode == KeyCode.LEFT) {
            if (currentColumn - 1 >= 0) {
                currentColumn -= 1;
                moveCell();
            }
        } else if (keyCode.isDigitKey()) {
            if(cells[currentRow][currentColumn].getIsHardcoded()){
                return;
            }
            String c = keyCode.getChar();
            if (keyCode.isKeypadKey()) {
                int pos = c.charAt(0) - 'a' + 1;
                c = String.valueOf(pos);
            }

            if (!c.equals("0")) {
                Cell oldCell = new Cell(cells[currentRow][currentColumn]);
                cells[currentRow][currentColumn].enterNumber(c, notesOn);
                checkForInvalidCells();
                Cell newCell = new Cell(cells[currentRow][currentColumn]);
                history.addToUndo(currentRow, currentColumn,oldCell,newCell);

                if(isSolved()){
                    solved();
                }
            }
        } else if (keyCode == KeyCode.DELETE || keyCode == KeyCode.BACK_SPACE) {
            handleErase();
        }
    }

    public void handleNumpad(int num) {
        if (currentRow >= 0 && currentRow < GRID_SIZE && currentColumn >= 0 && currentColumn < GRID_SIZE) {
            Cell oldCell = new Cell(cells[currentRow][currentColumn]);
            cells[currentRow][currentColumn].enterNumber(String.valueOf(num), notesOn);
            checkForInvalidCells();
            Cell newCell = new Cell(cells[currentRow][currentColumn]);
            history.addToUndo(currentRow, currentColumn,oldCell,newCell);

            if(isSolved()){
                solved();
            }
        }
    }

    public void handleErase() {
        if (currentRow >= 0 && currentRow < GRID_SIZE && currentColumn >= 0 && currentColumn < GRID_SIZE) {
            Cell oldCell = new Cell(cells[currentRow][currentColumn]);
            if (!notesOn) {
                cells[currentRow][currentColumn].enterNumber("", false);
            } else {
                cells[currentRow][currentColumn].eraseNotes();
            }
            checkForInvalidCells();
            Cell newCell = new Cell(cells[currentRow][currentColumn]);
            history.addToUndo(currentRow, currentColumn,oldCell,newCell);

        }
    }

    public void handleUndo(){
        Move move = history.undo();

        if(move == null){
            return;
        }

        cells[move.getRow()][move.getColumn()].updateCell(move.getOldCell());
        checkForInvalidCells();
    }

    public void handleRedo(){
        Move move = history.redo();

        if(move == null){
            return;
        }

        cells[move.getRow()][move.getColumn()].updateCell(move.getNewCell());
        checkForInvalidCells();
    }

    /**
     * @param all - if false, the cells with color INVALID_CELL_COLOR are not changed.
     */
    public void removeAllBackgrounds(boolean all) {
        for (int i = 0; i < GRID_SIZE; i += 1) {
            for (int j = 0; j < GRID_SIZE; j += 1) {
                if(cells[i][j].getBackground() != null ) {
                    if(all){
                        cells[i][j].addBackground(null);
                    }
                    else{
                        if(!cells[i][j].getBackground().getFills().get(0).getFill().equals(Color.web(INVALID_CELL_COLOR))) {
                            cells[i][j].addBackground(null);
                        }
                    }

                }
            }
        }
    }

    public boolean isSolved(){
        Set<Integer>[] rows = new Set[GRID_SIZE];
        Set<Integer>[] cols = new Set[GRID_SIZE];
        Set<Integer>[] blocks = new Set[GRID_SIZE];

        for(int i = 0; i < GRID_SIZE; i+=1) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            blocks[i] = new HashSet<>();
        }

        for (int i = 0; i < GRID_SIZE; i+=1) {
            for (int j = 0; j < GRID_SIZE; j+=1) {

                int num = cells[i][j].getMainNumber();

                if (num == 0){
                    return false;
                }

                int k = 3 * (i /3) + (j / 3);

                if (rows[i].contains(num)) {
                    return false;
                }
                if (cols[j].contains(num)) {
                    return false;
                }
                if (blocks[k].contains(num)) {
                    return false;
                }

                rows[i].add(num);
                cols[j].add(num);
                blocks[k].add(num);
            }
        }
        return true;
    }

    /**
     * @return 2d array which contains only the hardcoded cells.
     */
    public int[][] getInitialGrid(){
        int arr[][] = new int[GRID_SIZE][GRID_SIZE];

        for(int i=0;i<GRID_SIZE;i+=1){
            for(int j=0;j<GRID_SIZE;j+=1){
                if(cells[i][j].getIsHardcoded()) {
                    arr[i][j] = cells[i][j].getMainNumber();
                }
            }
        }

        return arr;
    }

    /**
     * Responsible for handling the output after the Sudoku is solved.
     */
    private void solved(){
        header.stopTime();
        createAlert().showAndWait();
        solvedServerCallback.run();

        for(int i=0;i<GRID_SIZE;i+=1){
            for(int j=0;j<GRID_SIZE;j+=1){
                cells[i][j].hardcodeCell(cells[i][j].getMainNumber());
            }
        }
    }

    private void moveCell() {
        if(currentRow > GRID_SIZE|| currentRow < 0 || currentColumn > GRID_SIZE || currentColumn < 0){
            return;
        }

        removeAllBackgrounds(false);

        // color all cells in the current row
        for (int i = 0; i < GRID_SIZE; i += 1) {
            if (!isInvalidCell(i, currentColumn)) {
                cells[i][currentColumn].addBackground(ROW_COLUMN_SECTOR_COLOR);
            }
        }
        // color all cells in the current column
        for (int i = 0; i < GRID_SIZE; i += 1) {
            if(!isInvalidCell(currentRow,i)) {
                cells[currentRow][i].addBackground(ROW_COLUMN_SECTOR_COLOR);
            }
        }

        // color all cells in the current sector and all cells with the same number as the current one
        for (int i = 0; i < GRID_SIZE; i += 1) {
            for (int j = 0; j < GRID_SIZE; j += 1) {
                if(isInvalidCell(i,j)){
                    continue;
                }

                if (cells[i][j].getSector() == cells[currentRow][currentColumn].getSector()) {
                    cells[i][j].addBackground(ROW_COLUMN_SECTOR_COLOR);
                }

                if ((cells[i][j].getMainNumber() == cells[currentRow][currentColumn].getMainNumber()) && cells[i][j].getMainNumber() != 0) {
                    cells[i][j].addBackground(SAME_NUMBER_COLOR);
                }
            }
        }
        cells[currentRow][currentColumn].addBackground(CURRENT_CELL_COLOR);

        checkForInvalidCells();
    }

    private void setMouseClickEvent(Cell cell, int x, int y) {
        cell.setOnMouseClicked(mouseEvent -> {
            currentRow = x;
            currentColumn = y;
            moveCell();
        });
    }

    private Alert createAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful");
        alert.setHeaderText("You successfully solved the Sudoku!");
        alert.setContentText("Time: " + header.getElapsedTime() + "\n" + "Difficulty: " + header.getDifficultyLabel());
        alert.setGraphic(null);
        return alert;
    }

    /**
     * Checks if there are any invalid cells on the board and colors them.
     */
    private void checkForInvalidCells(){
        for(int i=0;i<GRID_SIZE;i+=1){
            for(int j=0;j<GRID_SIZE;j+=1){
                if(isInvalidCell(i,j)){
                    cells[i][j].addBackground(INVALID_CELL_COLOR);
                }
                else if(cells[i][j].getMainNumber() != 0 && cells[i][j].getBackground() != null && !cells[i][j].getBackground().
                        getFills().get(0).getFill().equals(Color.web(SAME_NUMBER_COLOR))){
                    cells[i][j].addBackground(ROW_COLUMN_SECTOR_COLOR);
                }
            }
        }
        cells[currentRow][currentColumn].addBackground(CURRENT_CELL_COLOR);
    }

    private boolean isInvalidCell(int row, int column){
        boolean result = false;
        if(cells[row][column].getMainNumber() == 0){
            return false;
        }

        for (int i = 0; i < GRID_SIZE; i += 1) {
            if(i != row && cells[i][column].getMainNumber() == cells[row][column].getMainNumber()) {
                result = true;
            }
            cells[i][column].eraseNote(cells[row][column].getMainNumber());
        }

        for (int i = 0; i < GRID_SIZE; i += 1) {
            if(i != column && cells[row][i].getMainNumber() == cells[row][column].getMainNumber()) {
                result = true;
            }
            cells[row][i].eraseNote(cells[row][column].getMainNumber());
        }

        int localSectorRow = row - row % 3;
        int localSectorColumn = column - column % 3;
        for(int i=localSectorRow;i<localSectorRow + 3;i+=1){
            for(int j=localSectorColumn;j<localSectorColumn + 3;j+=1){
                if( i != row && j != column && cells[i][j].getSector() == cells[row][column].getSector() && cells[i][j].getMainNumber() == cells[row][column].getMainNumber()){
                    result = true;
                }
                cells[i][j].eraseNote(cells[row][column].getMainNumber());
            }
        }
        return result;
    }
}

