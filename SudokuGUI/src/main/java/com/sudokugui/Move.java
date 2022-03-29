package com.sudokugui;

/**
 * The class represents a single board move - filling a cell, deleting a value from a cell, toggling notes on/off and
 * undo command.
 * Keeps track of the position, the starting state and the updated state of a cell.
 */
public class Move {
    private final int row;
    private final int column;
    private final Cell oldCell;
    private final Cell newCell;

    public Move(int row, int column, Cell oldCell, Cell newCell){
        this.row = row;
        this.column = column;
        this.oldCell = new Cell(oldCell);
        this.newCell = new Cell(newCell);
    }

    public Move(Move other){
        this.row = other.row;
        this.column = other.column;
        this.oldCell = new Cell(other.oldCell);
        this.newCell = new Cell(other.newCell);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Cell getOldCell() {
        return oldCell;
    }

    public Cell getNewCell() {
        return newCell;
    }
}
