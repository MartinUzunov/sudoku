package com.sudoku.server;

import com.sudoku.ServerInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {

    private final GenerateSudoku generateSudoku;

    public ServerImpl() throws RemoteException {
        super();
        generateSudoku = new GenerateSudoku();
    }

    @Override
    public int[][] generateSudoku(GenerateSudoku.Difficulty difficulty) throws RemoteException {
        return generateSudoku.generate(difficulty);
    }

    @Override
    public int[][] solveSudoku(int[][] grid) throws RemoteException {
        generateSudoku.solve(grid);
        return grid;
    }

    @Override
    public void writeToFile(String line) throws IOException {
        FileWriter fileWriter = new FileWriter("log.txt", true);
        fileWriter.write(line + "\n");
        fileWriter.close();
    }
}
