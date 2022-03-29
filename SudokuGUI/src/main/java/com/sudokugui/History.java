package com.sudokugui;

import java.util.Stack;

/**
 * Represents an Undo and Redo functionality for Moves on the Grid.
 */
public class History {
    private final Stack<Move> undoStack;
    private final Stack<Move> redoStack;

    public History(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void addToUndo(int row, int column, Cell oldCell, Cell newCell){
        redoStack.clear();
        undoStack.push(new Move(row, column, oldCell, newCell));
    }

    public Move undo(){
        if(undoStack.size() <= 0){
            return null;
        }

        redoStack.push(new Move(undoStack.peek()));

        return undoStack.pop();
    }

    public Move redo(){
        if(redoStack.size() <= 0){
            return null;
        }

        undoStack.push(new Move(redoStack.peek()));

        return redoStack.pop();
    }
}

