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

    void initialiseDatabase() {
        // tables map
        File databaseDirectory = new File(storageFolderPath);
        File[] fileList = databaseDirectory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                Table table = new Table(file.getName(), databaseDirectory.getName(), storageFolderPath);
                table.initialiseRowsAndCallInitialiseTokens();
                addTable(table);
            }
        }

    }

    public Table combineTablesIntoNewTable(String tableOneName, String tableTwoName, String attributeOneName, String attributeTwoName) {
        String combinedTableName = tableOneName + "." + tableTwoName;
        Table tableOne = tables.get(tableOneName).copy();
        Table tableTwo = tables.get(tableTwoName).copy();

        Map<Integer, Integer> tableJoinOnID = getMapOfTableJoinOnID(tableOne, tableTwo, attributeOneName, attributeTwoName);
        Table newTable = makeNewTableAndupdateAttributesForJoin(tableOne, tableTwo);

        // attempt at mapping. half working ish. need to do proper mappinng here for the joins after below function
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
        // remove chosen attributes

        setAllTableIds(newTable);
        newTable.removeAttribute(tableOneName + "." + attributeOneName);
        newTable.removeAttribute(tableTwoName + "." + attributeTwoName);
        newTable.printTable();
        return newTable;
    }

    void setAllTableIds(Table table) {
        int i = 0;
        for (Row row : table.getRows()) {
            row.changeId(getAndIncrementID());
            // Token token = new Token((int) getAndIncrementID(), 0);
            // row.changeValue();
        }
    }

    // edits all attributes and makes a copy of the
    Table makeNewTableAndupdateAttributesForJoin(Table tableOne, Table tableTwo) {
        // update row attributes and tokens
        for (Row row : tableOne.getRows()) {
            for (Attribute attribute : row.getAttributes()) {
                if (!attribute.getName().equals("id")) {
                    String newString = tableOne.getName() + "." + attribute.getName();
                    row.changeKeyInAttributesToValuesMap(attribute.getName(), newString);
                    row.changeAttributeName(attribute.getName(), newString);
                }
            }
        }
        for (Row row : tableTwo.getRows()) {
            for (Attribute attribute : row.getAttributes()) {
                if (!attribute.getName().equals("id")) {
                    String newString = tableTwo.getName() + "." + attribute.getName();
                    row.changeKeyInAttributesToValuesMap(attribute.getName(), newString);
                    row.changeAttributeName(attribute.getName(), newString);
                }
            }
        }
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
        for (int i = 0; i < newTable.getRows().size(); i++) {
            for (int j = 1; j < tableTwo.getAttributes().size(); j++) {
                newTable.getRowNumber(i).addAttribute(tableTwo.getAttributeAtIndex(j));
            }
        }
        return newTable;
    }


    Map<Integer, Integer> getMapOfTableJoinOnID(Table tableOne, Table tableTwo, String attributeOneName, String attributeTwoName) {
        // String dataType = attributeOne;
        // maps table one's rows to table two's rows
        boolean matchFound;
        Map<Integer, Integer> tableJoinOnID = new HashMap<>();
        for (Row rowOne : tableOne.getRows()) {
            matchFound = false;
            String tableOneTarget = rowOne.getValueFromAttribute(attributeOneName).getValue();
            for (Row rowTwo : tableTwo.getRows()) {
                String tableTwoCandidate = rowTwo.getValueFromAttribute(attributeTwoName).getValue();
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
