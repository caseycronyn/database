package edu.uob;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//populates the tables rows and columns by passing in the formatted string. this class also holds the data for rows and columns
//the columns are ordered
public class Table {
    ArrayList<HashMap<String, String>> entries = new ArrayList<>();
    ArrayList<String> attributes = new ArrayList<>();
    static ArrayList<String> commandHolder = new ArrayList<String>();
    String tableName;
    String databaseName;
    String storageFolderPath;

    //    TODO this looks a bit crap
    public Table(String tableName, String databaseName, String storageFolderPath) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.storageFolderPath = storageFolderPath;
        this.attributes.add("id");
    }

    //    not sure of the use of this ...
    public void initialiseEmptyTableWithAttributes(ArrayList<String> attributes) {
        StringBuilder firstLineOfTable = new StringBuilder();
        for (String attribute : attributes) {
            firstLineOfTable.append(attribute);
            firstLineOfTable.append("\t");
        }

        System.out.println(attributes);
//        System.out.println(firstLineOfTable.toString());
        attributes.add(firstLineOfTable.toString());
//        System.out.println(attributes);
    }

    public void setAttributes(List<Token> tokens) {
        for (Token token : tokens) {
            attributes.add(token.getName());
        }
    }

    public void setDatabaseName(String databaseNameIn) {
        databaseName = databaseNameIn;
    }

    public void initialiseTableFromFile() {
        readInFileAndPopulateArrayWithAllLines();
        populateAttributesAndEntriesFromFile();
    }

    public void populateAttributesAndEntriesFromFile() {
        try {
            populateAttributesFromFile();
            populateEntriesAndMapAttributesFromFile();
        }
//        TODO is this ok?
        catch (Exception e) {
            System.out.println("error in populateAttributesAndEntries: " + e);
        }

    }

    public void populateAttributesFromFile() {
        String command = commandHolder.get(0);
        String[] attributeArray = command.split("\t");
        attributes.addAll(List.of(attributeArray));
//        System.out.println(attributes);
    }

    public void readInFileAndPrint() throws IOException {
        File fileToOpen = new File(tableName + ".tab");
        FileReader reader = new FileReader(fileToOpen);
        BufferedReader buffReader = new BufferedReader(reader);
        String line;
        while ((line = buffReader.readLine()) != null) {
            System.out.println(line);
        }
        buffReader.close();
    }

    public void readInFileAndPopulateArrayWithAllLines() {
        BufferedReader buffReader = null;
        FileReader reader = null;
        File fileToOpen = new File(tableName + ".tab");
        try {
            reader = new FileReader(fileToOpen);
            buffReader = new BufferedReader(reader);
            String line;
            while ((line = buffReader.readLine()) != null) {
                commandHolder.add(line);
//            System.out.println(line);
            }
            buffReader.close();
        } catch (Exception e) {
            System.out.println("error in readInFileAndPopulateArrayWithAllLines: " + e);
        }
    }

    public void populateEntriesAndMapAttributesFromFile() {
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
        for (HashMap<String, String> row : entries) {
            System.out.println(row);
        }
    }

    public void writeTableToFileFromMemory() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(storageFolderPath + File.separator + databaseName + File.separator + tableName + ".tab");
        } catch (Exception e) {
            System.out.println("error in writeTableToFileFromMemory: " + e);
        }
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

    public void writeEmptyTableToFile() {
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createFile(Paths.get(storageFolderPath + File.separator + databaseName + File.separator + tableName + ".tab"));
        } catch (IOException ioe) {
            System.out.println("Can't seem to create file " + storageFolderPath);
        }
    }

    public void addNewAttribute(String attribute) {
        attributes.add(attribute);
        writeTableToFileFromMemory();
    }

    public void removeAttribute(String attribute) {
        attributes.remove(attribute);
        writeTableToFileFromMemory();
    }

//     public void addEntryToTable(List<Token> tokens, Integer newID) {
//         HashMap<String, String> row = new HashMap<>();
//         if ((tokens.size() + 1) != attributes.size()) {
//             return;
//         }
//         row.put("id", newID.toString());
//         tokens.add(0, newID.toString());
//         for (int i = 0; i < attributes.size(); i++) {
//             row.put(attributes.get(i), tokens.get(i));
//         }
// //        System.out.println(row);
//         entries.add(row);
//     }

    public void printTableToStdout() {
        String tabJoinedLine = String.join("\t", attributes);
        System.out.println(tabJoinedLine);
        for (HashMap<String, String> row : entries) {
            ArrayList<String> orderedValues = new ArrayList<>();
//            loop through attributes in order
            for (String attribute : attributes) {
                orderedValues.add(row.get(attribute));
            }
            String joinedLine = String.join("\t", orderedValues);
            System.out.println(joinedLine);
        }
    }
}