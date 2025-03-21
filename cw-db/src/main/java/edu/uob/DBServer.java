package edu.uob;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/** This class implements the DB server. */
public class DBServer {
    private static final char END_OF_TRANSMISSION = 4;
    private final DBManager DBManager;

    public static void main(String args[]) throws IOException {
        DBServer server = new DBServer();
        server.blockingListenOn(8888);
    }

 // NOTE double check the databases location before submission
    /**
    * KEEP this signature otherwise we won't be able to mark your submission correctly.
    */
    public DBServer() {
        DBManager = new DBManager();
        try {
            DBManager.initialiseServer();
        }
        catch(Exception e) {
            throw new RuntimeException("Can't initialise server "+ e);
        }
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming DB commands and carries out the required actions.
    */
    public String handleCommand(String command) {
        try {
            TokenBank tokenBank = new TokenBank(command);
            DbCommandFactory parser = new DbCommandFactory();
            DBCommand commandResult = parser.createCommand(tokenBank);
            return commandResult.executeCommand(this.DBManager, tokenBank);
        }
        catch (Exception e) {
            return String.format("[ERROR]: " + e.getMessage());
        }
    }


    //  === Methods below handle networking aspects of the project - you will not need to change these ! ===

    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.err.println("Server encountered a non-fatal IO error:");
                    e.printStackTrace();
                    System.err.println("Continuing...");
                }
            }
        }
    }

    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {

            System.out.println("Connection established: " + serverSocket.getInetAddress());
            while (!Thread.interrupted()) {
                String incomingCommand = reader.readLine();
                System.out.println("Received message: " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }
}
