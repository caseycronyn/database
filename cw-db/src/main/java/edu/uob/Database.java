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
    HashMap<String, Table> tables = new HashMap<>();
    String name;
    private String storageFolderPath;
    Integer ID;

    Database (String name, String storageFolderPath) {
        this.name = name;
        this.storageFolderPath = storageFolderPath;
        this.ID = 1;
//        createIDFolderAndFile();
        createDatabaseFolder();
    }

    public Table combineTablesIntoNewTable(String tableOneName, String tableTwoName, String attributeOneName, String attributeTwoName) {
        String combinedTableName = tableOneName + "." + tableTwoName;
        Table tableOne = tables.get(tableOneName).copy();
        Table tableTwo = tables.get(tableTwoName).copy();

        // change table attribute names and add them to new table
        for (int i = 1; i < tableOne.getAttributes().size(); i++) {
            tableOne.getAttributeAtIndex(i).setName(tableOne.getName() + "." + tableOne.getAttributeAtIndex(i).getName());
        }

        Table newTable = tableOne.copy();
        for (int i = 1; i < tableTwo.getAttributes().size(); i++) {
            tableTwo.getAttributeAtIndex(i).setName(tableTwo.getName() + "." + tableTwo.getAttributeAtIndex(i).getName());
            newTable.attributes.add(tableTwo.getAttributeAtIndex(i));
        }

        // update tokens to reflect attributes
        // tableOne.updateAttributesToValues();

        Map<Integer, Integer> tableJoinOnID = getMapOfTableJoinOnID(tableOne, tableTwo, attributeOneName, attributeTwoName);

        // attempt at mapping. half working ish. need to do proper mappinng here for the joins after below function
        int tableOneIndex = tableOne.getRowNumber(0).getId();
        // loop though row indices in table one
        for (int i = 0; i < tableOne.getRows().size(); i++) {
            // loop through attributes of table two
            for (int j = 1; j < tableTwo.getAttributes().size(); j++) {
                // add matching Token of mapped row to row i of new table
                Integer matchingIndex = tableJoinOnID.get(tableOneIndex);

                Row tableTwoRow = tableTwo.getRowByIndices(matchingIndex);

                String attributeName = tableTwo.getAttributeAtIndex(j).getName();
                Token matchingToken = tableTwoRow.getValueFromAttribute(attributeName);

                Row tableOneRow = tableOne.getRowNumber(i);
                tableOneRow.addValueToRow(attributeName, matchingToken);
                }
            tableOneIndex++;
            }


        /*
        (skipping id)
        for attributes in tableOne:
            add attributes excluding joined attribute
        for attributes in tableTwo:
            add attributes excluding joined attribute
        for rows in tableTwo:
            if attributeTwo == attributeOne:
                add match to hashmap using ID's

        for rows in tableOne:
            add rows
        for rows in tableTwo:
            add matching rows using hashmap
        reset ID's
         */
        // add id

        // we need the list in order and the attributes in order
        // so we should construct this
        // first of all lets do table one

        newTable.removeAttribute(tableOneName + "." + attributeOneName);
        newTable.removeAttribute(tableTwoName + "." + attributeTwoName);

        return newTable;
    }

    Map<Integer, Integer> getMapOfTableJoinOnID(Table tableOne, Table tableTwo, String attributeOneName, String attributeTwoName) {
        // String dataType = attributeOne;
        // maps table one's rows to table two's rows
        boolean matchFound;
        Map<Integer, Integer> tableJoinOnID = new HashMap<>();
        for (Row rowOne : tableOne.getRows()) {
            matchFound = false;
            String tableOneTarget = rowOne.getValueFromAttribute(attributeOneName).getName();
            for (Row rowTwo : tableTwo.getRows()) {
                String tableTwoCandidate = rowTwo.getValueFromAttribute(attributeTwoName).getName();
                if (!matchFound && tableOneTarget.equals(tableTwoCandidate)) {
                    int rowOneID = rowOne.getId();
                    int rowTwoID = rowTwo.getId();
                    tableJoinOnID.put(rowOneID, rowTwoID);
                    matchFound = true;
                }
            }
        }
        return tableJoinOnID;
    }

    void addAttributesToNewTable(Table tableOne, Table tableTwo, String attributeOneName, String attributeTwoName, Table table) {

        // Attribute id = new Attribute("id", "integerLiteral", 0);
        // table.addAttribute(id);
        // // add attributes
        // for (int i = 1; i < tableOne.getAttributes().size(); i++) {
        //     Attribute retrievedAttributeOne = tableOne.getAttributeAtIndex(i);
        //     if (!retrievedAttributeOne.getName().equals(attributeOneName)) {
        //         Attribute newAttribute = new Attribute(retrievedAttributeOne.getName(), retrievedAttributeOne.getDataType(), retrievedAttributeOne.getIndex());
        //         table.addAttribute(newAttribute);
        //         table.getAttribute(retrievedAttributeOne.getName()).setName(tableOne.getName() + "." + retrievedAttributeOne.getName());
        //     }
        // }
        // for (int i = 1; i < tableTwo.getAttributes().size(); i++) {
        //     Attribute retrievedAttributeTwo = tableTwo.getAttributeAtIndex(i);
        //     if (!retrievedAttributeTwo.getName().equals(attributeTwoName)) {
        //         Attribute newAttributeTwo = new Attribute(retrievedAttributeTwo.getName(), retrievedAttributeTwo.getDataType(), retrievedAttributeTwo.getIndex());
        //         table.addAttribute(newAttributeTwo);
        //         table.getAttribute(retrievedAttributeTwo.getName()).setName(tableTwo.getName() + "." + retrievedAttributeTwo.getName());
        //     }
        // }
    }

    void addTable(Table table) {
        tables.put(table.getName(), table);
    }

//     public void addNewTableFromFile(String tableName) {
//         Table table = new Table(tableName, name, storageFolderPath);
//         table.initialiseTableFromFile();
//         tables.put(tableName, table);
//     }

//    public void createIDFolderAndFile() {
//        try {
//            // Create the database storage folder if it doesn't already exist !
//            Files.createDirectories(Paths.get(storageFolderPath + File.separator + "state"));
//            Files.createFile(Paths.get(storageFolderPath + File.separator + "state" + File.separator + "id"));
//        } catch(IOException ioe) {
//            System.out.println("Can't seem to create state storage folder " + storageFolderPath);
//        }
//    }

    public void createDatabaseFolder() {
        //        create database path
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath + File.separator + name));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath + File.separator + name);
        }
    }

    public int getAndIncrementID() {
        return ID++;
    }

}
