package edu.uob;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// formats the string then passes it into the appropriate table method
// all these will need to be made more robust at a later point
public class Database {
    HashMap<String, Table> tableMap = new HashMap<>();
    String name, storageFolderPath;
    Integer ID;

    Database (String name, String storageFolderPath) {
        this.name = name;
        this.storageFolderPath = storageFolderPath;
        this.ID = 1;
//        createIDFolderAndFile();
        try {
            createDBFolder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean tableExists(String tableName) {
        return tableMap.containsKey(tableName);
    }

    String getName() {
        return name;
    }

    Table getTable(String tableName) {
        return tableMap.get(tableName);
    }

    void initialiseDatabase() {
        // tables map
        File dbDirectory = new File(storageFolderPath + File.separator + name);
        File[] fileList = dbDirectory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                String tableName = file.getName().replace(".tab", "");
                // avoid dotfiles like .DSStore
                if (tableName.charAt(0) != '.') {
                    Table newTable = new Table(tableName, dbDirectory.getName(), storageFolderPath);
                    try {
                        newTable.initialiseTable(this);
                    }
                    catch(Exception e) {
                        System.err.println("Error while initialising table " + tableName);
                    }
                    addTable(newTable);
                }
            }
        }

    }

    public Table joinTables(String tableOneName, String tableTwoName, String attributeOneName, String attributeTwoName) {
        Table tableOne = tableMap.get(tableOneName).copy();
        Table tableTwo = tableMap.get(tableTwoName).copy();

        Map<Integer, Integer> indicesMap = mapTwoTables(tableOne, tableTwo, attributeOneName, attributeTwoName);
        Table newTable = joinTableAttributes(tableOne, tableTwo);

        mapTablesIndices(newTable, tableOne, tableTwo, indicesMap);

        initialiseTableIDs(newTable);
        newTable.removeAttribute(tableOneName + "." + attributeOneName);
        newTable.removeAttribute(tableTwoName + "." + attributeTwoName);
        return newTable;
    }

    void mapTablesIndices(Table newTable, Table tableOne, Table tableTwo, Map<Integer, Integer> tableJoinOnID) {
        // mapping
        int tableOneIndex = tableOne.getRowNumber(0).getId();
        // loop though rows in table one
        for (int i = 0; i < tableOne.getRows().size(); i++) {
            // loop through attributes of table two skipping id
            for (int j = 1; j < tableTwo.getAttributes().size(); j++) {
                // add matching Token of mapped row to row i of new table
                Integer tableTwoIndex = tableJoinOnID.get(tableOneIndex);
                Row tableTwoRow = tableTwo.getRowByIndices(tableTwoIndex);

                String attributeName = tableTwo.getAttributeAtIndex(j).getName();
                Token matchingToken = tableTwoRow.getValueFromAttribute(attributeName);

                Row tableOneRow = newTable.getRowNumber(i);
                tableOneRow.addValueToRow(attributeName, matchingToken);
            }
            tableOneIndex++;
        }


    }

    void initialiseTableIDs(Table table) {
        for (Row row : table.getRows()) {
            row.changeId(getNewID());
        }
    }

    // edits all attributes and makes a copy of the
    Table joinTableAttributes(Table tableOne, Table tableTwo) {
        // update row attributes and tokens
        updateRowAttributes(tableOne);
        updateRowAttributes(tableTwo);
        // change table attribute names
        for (int i = 1; i < tableOne.getAttributes().size(); i++) {
            tableOne.getAttributeAtIndex(i).setName(tableOne.getName() + "." + tableOne.getAttributeAtIndex(i).getName());
        }
        // create table and add tableTwo's attributes
        Table newTable = tableOne.copy();
        for (int i = 1; i < tableTwo.getAttributes().size(); i++) {
            tableTwo.getAttributeAtIndex(i).setName(tableTwo.getName() + "." + tableTwo.getAttributeAtIndex(i).getName());
            newTable.attributes.add(tableTwo.getAttributeAtIndex(i));
        }
        return newTable;
    }

    void updateRowAttributes (Table table) {
        for (Row row : table.getRows()) {
            for (Attribute attribute : table.getAttributes()) {
                if (!attribute.getName().equals("id")) {
                    String newString = table.getName() + "." + attribute.getName();
                    row.changeKeyInAttributesToValuesMap(attribute.getName(), newString);
                    // row.changeAttributeName(attribute.getName(), newString);
                }
            }
        }
    }

    Map<Integer, Integer> mapTwoTables(Table tableOne, Table tableTwo, String attributeOneName, String attributeTwoName) {
        // maps table one's rows to table two's rows
        boolean matchFound;
        Map<Integer, Integer> tableJoinMap = new HashMap<>();
        for (Row rowOne : tableOne.getRows()) {
            matchFound = false;
            String tableOneTarget = rowOne.getValueFromAttribute(attributeOneName).getValue();
            for (Row rowTwo : tableTwo.getRows()) {
                String tableTwoCandidate = rowTwo.getValueFromAttribute(attributeTwoName).getValue();
                if (!matchFound && tableOneTarget.equals(tableTwoCandidate)) {
                    int rowOneID = rowOne.getId();
                    int rowTwoID = rowTwo.getId();
                    tableJoinMap.put(rowOneID, rowTwoID);
                    matchFound = true;
                }
            }
        }
        return tableJoinMap;
    }

    void addTable(Table table) {
        tableMap.put(table.getName(), table);
    }

    public void createDBFolder() throws IOException {
        try {
            Files.createDirectories(Paths.get(storageFolderPath + File.separator + name));
        } catch(IOException ioe) {
            throw new IOException("Can't seem to create database storage folder " + storageFolderPath + File.separator + name);
        }
    }

    public int getNewID() {
        return ID++;
    }

}
