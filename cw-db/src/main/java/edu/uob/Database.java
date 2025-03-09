package edu.uob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

// formats the string then passes it into the appropriate table method
// all these will need to be made more robust at a later point
public class Database {
    HashMap<String, Table> tables = new HashMap<>();
    String name;
    private String storageFolderPath;

    Database (String name, String storageFolderPath) {
        this.name = name;
        this.storageFolderPath = storageFolderPath;
//        create database path
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath + File.separator + name));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
    }
//
    public void addNewTableFromFile(String tableName) {
        Table table = new Table(tableName, name, storageFolderPath);
        table.initialiseTableFromFile();
        tables.put(tableName, table);
    }
}
