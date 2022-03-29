package com.sudoku;

import com.sudoku.server.GenerateSudoku;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    int[][] generateSudoku(GenerateSudoku.Difficulty difficulty) throws RemoteException;

    int[][] solveSudoku(int[][] grid) throws RemoteException;

    void writeToFile(String line) throws IOException;
}
