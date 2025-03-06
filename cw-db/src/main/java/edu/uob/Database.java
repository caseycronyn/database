package edu.uob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

// formats the string then passes it into the appropriate table method
// all these will need to be made more robust at a later point
public class Database {
    ArrayList<Table> tables = new ArrayList<>();
    String name;
    private String storageFolderPath;

    Database (String name) {
        this.name = name;
//        create path
        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath+ File.separator + name));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
    }

    public void createNewTable(String tableName) {
        Table table = new Table(tableName, name);
        tables.add(table);
    }
//    public static void main(String[] args) throws IOException {
//        Table table = new Table("sheds");
//        table.readInFileAndPopulateArray();
//        table.populateAttributes();
//        table.populateEntriesAndMapAttributes();
//        table.writeTableToFile();
//        Tokenizer tokenizer = new Tokenizer("CREATE DATABASE ALPACAS;");
//    }

}
