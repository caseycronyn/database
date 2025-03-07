package edu.uob;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

/** This class implements the DB server. */
public class DBServer {
    HashMap<String, Database> databases = new HashMap();
    String currentDatabase = null;

    private static final char END_OF_TRANSMISSION = 4;
    private String storageFolderPath;
    DBCommand commandResult;

    public static void main(String args[]) throws IOException {
        DBServer server = new DBServer();
//        server.blockingListenOn(8888);
    }

//  NOTE double check the databases location before submission
    /**
    * KEEP this signature otherwise we won't be able to mark your submission correctly.
    */
    public DBServer() {
        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
    }

////    setup database?
//    public void setup() {
//    }

    public void createNewDatabase(String databaseName) {
        Database database = new Database(databaseName, storageFolderPath);
        databases.put(databaseName, database);
    }

    public void setCurrentDatabase(String databaseName) {
        currentDatabase = databaseName;
    }

    public String getCurrentDatabase() {
        return currentDatabase;
    }

    public void createNewTable(String tableName, String databaseName) {
        Table table = new Table(tableName, databaseName);
        table.initialise();
        databases.get(databaseName).addNewTable(tableName);
//        databaseName.addNewtable(tableName);
    }
    /**
    * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming DB commands and carries out the required actions.
    */
    public String handleCommand(String command) {
        Tokeniser tokeniser = new Tokeniser();
        Parser parser = new Parser();
        TokenBank tokenBank = new TokenBank();

        tokenBank.setTokens(tokeniser.setup(command));
        commandResult = parser.parse(tokenBank);
        commandResult.executeCommand(this);
        return "";
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
