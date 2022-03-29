package com.sudoku.server;

import com.sudoku.ServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerMain {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        ServerInterface obj = new ServerImpl();

        LocateRegistry.createRegistry(4999);
        Naming.rebind("rmi://localhost:4999" + "/sudoku", obj);

        System.out.println("Bound!");
        System.out.println("Server will wait forever for messages.");
    }
}
