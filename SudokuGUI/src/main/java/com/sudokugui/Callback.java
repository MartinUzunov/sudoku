package com.sudokugui;

/**
 * Helper class which contains callback functions to different elements in the Control Panel.
 */
public interface Callback {
    void numpadCall(int num);

    void notesCall();

    void eraseCall();

    void undoCall();

    void redoCall();

    void solveCall();
}