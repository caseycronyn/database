package edu.uob;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

    public String getStorageFolderPath() {
        return storageFolderPath;
    }

    public void deleteDirectory(File file) {
        // function to delete subdirectories and files
        // store all the paths of files and folders present
        // inside directory
        for (File subfile : file.listFiles()) {
            // if it is a subfolder,e.g Rohan and Ritik,
            //  recursively call function to empty subfolder
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            // delete files and empty subfolders
            subfile.delete();
        }
    }

    public void setCurrentDatabase(String databaseName) {
        currentDatabase = databaseName;
    }

    public String getCurrentDatabase() {
        return currentDatabase;
    }

    public void createNewTableFromFile(String tableName, String databaseName) {
        Table table = new Table(tableName, databaseName, storageFolderPath);
        table.initialiseTableFromFile();
        table.writeTableToFileFromMemory();
        databases.get(databaseName).addNewTableFromFile(tableName);
//        databaseName.addNewtable(tableName);
    }

//    public void removeTable(String tableName) {
//        databases.get(currentDatabase).
//    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming DB commands and carries out the required actions.
    */
    public String handleCommand(String command) {
        Tokeniser tokeniser = new Tokeniser();
        TokenBank tokenBank = new TokenBank();
        Parser parser = new Parser();

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
