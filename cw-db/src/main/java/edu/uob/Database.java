package edu.uob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// formats the string then passes it into the appropriate table method
// all these will need to be made more robust at a later point
public class Database {
    ArrayList<Table> tables = new ArrayList<>();

    public void createTable(String tableName) {
        Table table = new Table(tableName);
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
