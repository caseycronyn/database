package edu.uob;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/** This class implements the DB server. */
public class DBServer {
    Map<String, Database> databases = new HashMap<>();
    String currentDatabase = null;

    private static final char END_OF_TRANSMISSION = 4;
    private final String storageFolderPath;

    public static void main(String args[]) throws IOException {
        DBServer server = new DBServer();
        server.blockingListenOn(8888);
    }

 // NOTE double check the databases location before submission
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
        try {
            initialiseServer();
        }
        catch(Exception e) {
            System.out.println("Can't initialise server " + storageFolderPath + ": " + e);
        }
    }

    public String getStorageFolderPath() {
        return storageFolderPath;
    }

    boolean databaseExists(String databaseName) {
        return databases.containsKey(databaseName);
    }

    public void initialiseServer() throws NullPointerException{
        // databases map
        File storageDirectory = new File(storageFolderPath);
        File[] fileList = storageDirectory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                // avoid hidden folders
                if (!file.getName().startsWith(".")) {
                    Database database = new Database(file.getName(), storageFolderPath);
                    try {
                        database.initialiseDatabase();
                    }
                    catch(Exception e) {
                        System.out.println("Can't initialise database " + file.getName());
                    }
                    databases.put(file.getName(), database);
                }
            }
        }
    }

    public void deleteDirectory(File filePointer) {
        File[] files = (filePointer.listFiles());
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                }
                if (!file.delete()) {
                    System.out.println("Error deleting " + file.getName());
                }
            }
        }
    }

    public void setCurrentDatabase(String databaseName) {
        currentDatabase = databaseName;
    }

    public String getCurrentDatabase() {
        return currentDatabase;
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming DB commands and carries out the required actions.
    */
    public String handleCommand(String command) {
        Tokeniser tokeniser = new Tokeniser();
        TokenBank tokenBank = new TokenBank(tokeniser.getListOfTokens(command));
        try {
            new Lexer(tokenBank);
        }
        catch (Exception e) {
            return "[ERROR] found during lexing: " + e.getMessage();
        }
        Parser parser = new Parser();
        try {
            DBCommand commandResult = parser.parse(tokenBank);
            return commandResult.executeCommand(this, tokenBank);
        }
        catch (FileNotFoundException e) {
            return "[ERROR]: " + e.getMessage();
        }
        catch (Exception e) {
            return "[ERROR]: " + e.getMessage();
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
