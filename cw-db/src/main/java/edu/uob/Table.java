package edu.uob;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//populates the tables rows and columns by passing in the formatted string. this class also holds the data for rows and columns
//the columns are ordered
public class Table {
    // List<HashMap<String, Token>> rowsOfAttributesMappedToTokens = new ArrayList<>();
    List<Attribute> attributes = new ArrayList<>();
    Map<String, String> attributesToValues = new HashMap<>();
    List<Row> rows = new ArrayList<>();

    static ArrayList<String> commandHolder = new ArrayList<String>();
    String tableName;
    String databaseName;
    String storageFolderPath;

    //    TODO this looks a bit crap
    public Table(String tableName, String databaseName, String storageFolderPath) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.storageFolderPath = storageFolderPath;
        // this.attributes.add("id");
    }

    void updateTable() {
        writeTableToFileFromMemory();
        updateAttributesToValues();
    }

    void updateAttributesToValues() {
        Map<String, String> valueSet = new HashMap<>();
        String value, finalValue;
        value =  finalValue = null;
        for (int i = 1; i < attributes.size(); i++) {
            valueSet.clear();
            for (int j = 0; j < rows.size(); j++) {
                value = rows.get(j).attributesToValues.get(attributes.get(i).getName()).getDataType();
                valueSet.put(value, value);
            }
        if (valueSet.size() == 1) {
            finalValue = value;
        }
        else if (valueSet.size() > 1) {
            finalValue = resolveValueConflict(valueSet);
        }
        attributes.get(i).setDataType(finalValue);
        }
    }

    // string + anything = string
    // bool + anything = string
    // float + int = float
    String resolveValueConflict(Map<String, String> valueSet) {
        if (valueSet.containsKey("floatLiteral") && valueSet.containsKey("stringLiteral")) {
            return "floatLiteral";
        }
        else return "stringLiteral";
    }

    // set all to null
    void addAttributesToTable(List<Token> tokens) {
        Attribute idAttribute = new Attribute("id", "integerLiteral");
        attributes.add(idAttribute);
        for (Token token : tokens) {
            Attribute attribute = new Attribute(token.getName(), "NULL");
            attributes.add(attribute);
        }
        updateTable();
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

    // public void setAttributes(List<Token> tokens) {
    //     for (Token token : tokens) {
    //         attributes.add(token.getName());
    //     }
    // }

    public void setDatabaseName(String databaseNameIn) {
        databaseName = databaseNameIn;
    }

    // public void initialiseTableFromFile() {
    //     readInFileAndPopulateArrayWithAllLines();
    //     populateAttributesAndEntriesFromFile();
    // }
    //
//     public void populateAttributesAndEntriesFromFile() {
//         try {
//             populateAttributesFromFile();
//             populateEntriesAndMapAttributesFromFile();
//         }
// //        TODO is this ok?
//         catch (Exception e) {
//             System.out.println("error in populateAttributesAndEntries: " + e);
//         }
//
//     }

//     public void populateAttributesFromFile() {
//         String command = commandHolder.get(0);
//         String[] attributeArray = command.split("\t");
//         attributes.addAll(List.of(attributeArray));
// //        System.out.println(attributes);
//     }

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

//     public void populateEntriesAndMapAttributesFromFile() {
// //        loop through each line:
//         for (int i = 1; i < commandHolder.size(); i++) {
// //            split words into separate terms add to word_array
//             String[] entryArray = commandHolder.get(i).split("\t");
//             HashMap<String, Token> row = new HashMap<>();
//             if (entryArray.length != attributes.size()) {
//                 return;
//             }
// //            loop through word_array:
//             for (int j = 0; j < attributes.size(); j++) {
// //                add each key value pair to a new row
//                 row.put(attributes.get(j), entryArray[j]);
//             }
//             entries.add(row);
// //            System.out.println(row);
// //            System.out.println(commandHolder.get(i));
//         }
// //        for (String entry: commandHolder) {
// //            System.out.println(entry);
// //        }
//     }

    public void printEntriesHashMap() {
        for (Row row : rows) {
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
        StringBuilder firstLine = new StringBuilder();
        for (Attribute attribute : attributes) {
            firstLine.append(attribute.getName()).append("\t");
        }
        writer.println(firstLine);
        /*
        for each row of entries:
            make ordered array
            join with tabs
            write to file
         */
        ArrayList<String> orderedValues = new ArrayList<>();
        for (Row row : rows) {
            orderedValues.clear();
//            loop through attributes in order
            for (Attribute attribute : attributes) {
                orderedValues.add(row.attributesToValues.get(attribute.getName()).getStringValue());
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

    public void addNewAttribute(String name) {
        Attribute attribute = new Attribute(name, "NULL");
        attributes.add(attribute);
        writeTableToFileFromMemory();
    }

    public void removeAttribute(String name) {
        Iterator<Attribute> attribute = attributes.iterator();
        while (attribute.hasNext()) {
            if (attribute.next().getName().equals(name)) {
                attribute.remove();
            }
        }
        updateTable();
    }

    public void addRowToTable(List<Token> valueList, Integer newID) {
        Row row = new Row(attributes, valueList, newID);
        rows.add(row);
        checkAndSetValueTypes();
        updateTable();
    }

    public void checkAndSetValueTypes() {
        for (Attribute attribute : attributes) {
            ArrayList<String> valueTypes = new ArrayList();
            for (int i = 1; i < rows.size(); i++) {
            }
        }
    }

    public void printTableToStdout(List<Token> selectedAttributes, List<Token> condition) {
        if (selectedAttributes == null) {
            selectedAttributes = new ArrayList<>();
            for (Attribute attribute : attributes) {
                Token token = new Token(attribute.getName(), -1);
                selectedAttributes.add(token);
            }
        }
        StringBuilder firstLine = new StringBuilder();
        for (Token attribute : selectedAttributes) {
            firstLine.append(attribute.getName()).append("\t");
        }
        System.out.println(firstLine);
        for (Row row : rows) {
            if (conditionIsMet(condition, row)) {
                ArrayList<String> orderedValues = new ArrayList<>();
                // loop through attributes in order
                for (Token attribute : selectedAttributes) {
                    String attributeValue = row.attributesToValues.get(attribute.getName()).getStringValue();
                    orderedValues.add(attributeValue);
                }
                String joinedLine = String.join("\t", orderedValues);
                System.out.println(joinedLine);
            }
        }
    }

    boolean conditionIsMet(List<Token> condition, Row row) {
        return true;
        // if (condition == null) {
        //     return true;
        // }
        // else {

        // for (Token token : condition) {
        //     ;
        // }

        // }
        //
        // return false;
    }
}