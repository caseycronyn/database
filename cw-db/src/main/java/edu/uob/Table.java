package edu.uob;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

//populates the tables rows and columns by passing in the formatted string.
//the columns are ordered
public class Table {
    ArrayList<String> attributes = new ArrayList<>();
    ArrayList<HashMap<String, String>> entries = new ArrayList<>();
    static ArrayList<String> commandHolder = new ArrayList<String>();
    String name;

    public Table(String name) {
        this.name = name;
    }
    public void populateAttributes() {
        String command = commandHolder.get(0);
        String[] attributeArray = command.split("\t");
        attributes.addAll(List.of(attributeArray));
//        System.out.println(attributes);
    }

    public void readInFileAndPrint() throws IOException {
        File fileToOpen = new File(this.name + ".tab");
        FileReader reader = new FileReader(fileToOpen);
        BufferedReader buffReader = new BufferedReader(reader);
        String line;
        while((line = buffReader.readLine()) != null) {
            System.out.println(line);
        }
        buffReader.close();
    }

    public void readInFileAndPopulateArray() throws IOException {
        File fileToOpen = new File(this.name + ".tab");
        FileReader reader = new FileReader(fileToOpen);
        BufferedReader buffReader = new BufferedReader(reader);
        String line;
        while ((line = buffReader.readLine()) != null) {
            commandHolder.add(line);
//            System.out.println(line);
        }
        buffReader.close();
    }
    public void populateEntriesAndMapAttributes() {
//        loop through each line:
        for (int i = 1; i < commandHolder.size(); i++) {
//            split words into separate terms add to word_array
            String[] entryArray = commandHolder.get(i).split("\t");
            HashMap<String, String> row = new HashMap<>();
            if (entryArray.length != attributes.size()) {
                return;
            }
//            loop through word_array:
            for (int j = 0; j < attributes.size(); j++) {
//                add each key value pair to a new row
                row.put(attributes.get(j), entryArray[j]);
            }
            entries.add(row);
//            System.out.println(row);
//            System.out.println(commandHolder.get(i));
        }
//        for (String entry: commandHolder) {
//            System.out.println(entry);
//        }
    }

    public void printEntriesHashMap() {
        for (HashMap<String, String> row: entries) {
            System.out.println(row);
        }
    }

    public void writeTableToFile() throws IOException {
        PrintWriter writer = new PrintWriter("databases" + File.separator + this.name + ".tab");
        String tabJoinedLine = String.join("\t", attributes);
        writer.println(tabJoinedLine);
        /*
        for each row of entries:
            make ordered array
            join with tabs
            write to file
         */
        for (HashMap<String, String> row : entries) {
            ArrayList<String> orderedValues = new ArrayList<>();
//            loop through attributes in order
            for (String attribute : attributes) {
                orderedValues.add(row.get(attribute));
            }
            String joinedLine = String.join("\t", orderedValues);
//            System.out.println(joinedLine);
            writer.println(joinedLine);
        }
//        for (String attribute : attributes) {
//        System.out.println(attribute);
//        writer.println(attribute);
        writer.close();
    }
}
