package edu.uob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// formats the string then passes it into the appropriate table method
// all these will need to be made more robust at a later point
public class Database {
    static ArrayList<String> commandHolder = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        Database database = new Database();
        Table table = new Table();
        database.readInFileAndPopulateArray("people.tab");
        table.populateAttributes(commandHolder.get(0));
        table.populateEntriesAndMapAttributes(commandHolder);
    }

    public void readInFileAndPrint(String name) throws IOException {
        File fileToOpen = new File(name);
        FileReader reader = new FileReader(fileToOpen);
        BufferedReader buffReader = new BufferedReader(reader);
        String line;
        while((line = buffReader.readLine()) != null) {
            System.out.println(line);
        }
        buffReader.close();
    }

    public void readInFileAndPopulateArray(String name) throws IOException {
        File fileToOpen = new File(name);
        FileReader reader = new FileReader(fileToOpen);
        BufferedReader buffReader = new BufferedReader(reader);
        String line;
        while ((line = buffReader.readLine()) != null) {
            commandHolder.add(line);
//            System.out.println(line);
        }
        buffReader.close();
    }
}
