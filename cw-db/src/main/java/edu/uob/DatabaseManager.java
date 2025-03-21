package edu.uob;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private final String storageFolderPath;
    Map<String, Database> databases;
    Database currentDatabase;

    void addDatabase(Database database) {
        databases.put(database.getName(), database);
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
                    addDatabase(database);
                }
            }
        }
    }

    public DatabaseManager() {
        this.databases = new HashMap<>();
        this.currentDatabase = null;

        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();

        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }

    }

    public void setCurrentDatabase(String databaseName) {
        if (databaseExists(databaseName)) {
            currentDatabase = this.databases.get(databaseName);
        }
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public String getStorageFolderPath() {
        return storageFolderPath;
    }

    boolean databaseExists(String databaseName) {
        return databases.containsKey(databaseName);
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


}
