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
    // Map<String, String> attributesToValues = new HashMap<>();
    List<Row> rows = new ArrayList<>();

    // ArrayList<String> commandHolder = new ArrayList<>();
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

    Table copy() {

        List<Attribute> newAttributes = new ArrayList<>();
        for (Attribute attribute : attributes) {
            newAttributes.add(attribute.copy());
        }

        List<Row> newRows = new ArrayList<>();
        for (Row row : rows) {
            newRows.add(row.copy());
        }

        String newName = new String(tableName);
        String newDatabaseName = new String(databaseName);
        String newStorageFolderPath = new String(storageFolderPath);

        Table newTable = new Table(newName, newDatabaseName, newStorageFolderPath);
        newTable.addRows(newRows);
        newTable.addAttributes(newAttributes);

        return newTable;
    }

    String getDatabaseName() {
        return databaseName;
    }

    String getStorageFolderPath() {
        return storageFolderPath;
    }

    void addRows(List<Row> rowsIn) {
        rows.addAll(rowsIn);
    }

    void addAttributes(List<Attribute> attributesIn) {
        attributes.addAll(attributesIn);
    }

    Attribute getAttribute(String name) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    String getName() {
        return tableName;
    }

    List<Row> getRows() {
        return rows;
    }

    Attribute getAttributeAtIndex(int index) {
        return attributes.get(index);
    }

    void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    List<Attribute> getAttributes() {
        return attributes;
    }

    void updateTable() throws FileNotFoundException {
        writeTableToFileFromMemory();
        updateAttributesToValues();
    }

    void updateAttributesToValues() {
        Map<String, String> valueSet = new HashMap<>();
        String value, finalValue;
        value = finalValue = null;
        for (int i = 1; i < attributes.size(); i++) {
            valueSet.clear();
            for (int j = 0; j < rows.size(); j++) {
                value = rows.get(j).attributesToValues.get(attributes.get(i).getName()).getTokenType();
                valueSet.put(value, value);
            }
            if (valueSet.size() == 1) {
                finalValue = value;
            } else if (valueSet.size() > 1) {
                finalValue = resolveValueConflict(valueSet);
            }
            attributes.get(i).setDataType(finalValue);
        }
    }

    void updateTokenToCurrentAttributeNames() {
        for (Row row : rows) {
            for (Attribute attribute : row.getAttributes()) {

            }
        }
    }

    // string + anything = string
    // bool + anything = string
    // float + int = float
    String resolveValueConflict(Map<String, String> valueSet) {
        if (valueSet.containsKey("floatLiteral") && valueSet.containsKey("stringLiteral")) {
            return "floatLiteral";
        } else return "stringLiteral";
    }

    void changeValuesInTableWhereCondition(List<Token> nameVauePairs, List<Token> conditions) throws FileNotFoundException {
        for (Row row : rows) {
            if (conditionIsMet(conditions, row)) {
                updateValuesInRow(nameVauePairs, row);
            }
        }
        updateTable();
    }

    void updateValuesInRow(List<Token> nameValuePairs, Row row) {
        for (int i = 0; i < nameValuePairs.size(); i += 3) {
            Token nameToken = nameValuePairs.get(i);
            Token valueToken = nameValuePairs.get(i + 2);
            row.changeValue(nameToken.getValue(), valueToken);
        }
    }

    // set all to null
    void addAttributesToTable(List<Token> tokens) throws FileNotFoundException {
        int i = 0;
        Attribute idAttribute = new Attribute("id", "integerLiteral", i);
        attributes.add(idAttribute);
        for (Token token : tokens) {
            Attribute attribute = new Attribute(token.getValue(), "NULL", ++i);
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

//     public void populateAttributesAndEntriesFromFile() {
//         try {
//             populateAttributesFromFile();
//             populateEntriesAndMapAttributesFromFile();
//         }
// //        TODO is this ok?
//         catch (Exception e) {
//             System.out.println("error in populateAttributesAndEntries: " + e);
//         }

    // }

//     public void populateAttributesFromFile() {
//         String command = commandHolder.get(0);
//         List<String> attributeArray = new ArrayList<>();
//         attributeArray.addAll(List.of(command.split("\t")));
//         int i = 0;
//         for (String attribute : attributeArray) {
//             Attribute newAttribute = new Attribute(attribute, "NULL", i++);
//             attributes.add(newAttribute);
//         }
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

//     public void readInFileAndPopulateArrayWithAllLines() {
//         commandHolder.clear();
//         BufferedReader buffReader = null;
//         FileReader reader = null;
//         File fileToOpen = new File(storageFolderPath + File.separator + databaseName + File.separator + tableName + ".tab");
//         try {
//             reader = new FileReader(fileToOpen);
//             buffReader = new BufferedReader(reader);
//             String line;
//             while ((line = buffReader.readLine()) != null) {
//                 commandHolder.add(line);
// //            System.out.println(line);
//             }
//             buffReader.close();
//         } catch (Exception e) {
//             System.out.println("error in readInFileAndPopulateArrayWithAllLines: " + e);
//         }
//     }

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
// //                 row.put(attributes.get(j), entryArray[j]);
//             }
//             rows.add(row);
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

    public void writeTableToFileFromMemory() throws FileNotFoundException {
        String newFileName = storageFolderPath + File.separator + databaseName + File.separator + tableName + ".tab";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(newFileName);
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException("error writing to file " + newFileName);
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
                orderedValues.add(row.attributesToValues.get(attribute.getName()).getValue());
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

    public void addNewAttribute(String name) throws FileNotFoundException {
        Attribute attribute = new Attribute(name, "NULL", attributes.size());
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
        // updateTable();
    }

    public void addRowToTable(List<Token> valueList, Integer newID) throws FileNotFoundException {
        Row row = new Row(attributes, valueList, newID);
        rows.add(row);
        checkAndSetValueTypes();
        updateTable();
    }

    Row getRowNumber(int index) {
        return rows.get(index);
    }

    Row getRowByIndices(int index) {
        int match = -1;
        for (Row row : rows) {
            if (row.getValueFromAttribute("id").getIntegerValue() == index) {
                return row;
            }
        }
        return null;
    }


    public void checkAndSetValueTypes() {
        for (Attribute attribute : attributes) {
            ArrayList<String> valueTypes = new ArrayList();
            for (int i = 1; i < rows.size(); i++) {
            }
        }
    }

    // this needs to be changed so we spit the text back
    public String filterTableWithAttributesAndCondition(List<Token> selectedAttributes, List<Token> condition) {
        StringBuilder output = new StringBuilder();
        if (selectedAttributes == null) {
            selectedAttributes = new ArrayList<>();
            for (Attribute attribute : attributes) {
                Token token = new Token(attribute.getName(), -1);
                selectedAttributes.add(token);
            }
        }
        StringBuilder firstLine = new StringBuilder();
        for (Token attribute : selectedAttributes) {
            firstLine.append(attribute.getValue()).append("\t");
        }
        // System.out.println(firstLine);
        output.append(firstLine.append("\n"));
        for (Row row : rows) {
            if (condition == null || conditionIsMet(condition, row)) {
                ArrayList<String> orderedValues = new ArrayList<>();
                // loop through attributes in order
                for (Token attribute : selectedAttributes) {
                    String attributeValue = row.attributesToValues.get(attribute.getValue()).getValue();
                    orderedValues.add(attributeValue);
                }
                String joinedLine = String.join("\t", orderedValues);
                output.append(joinedLine).append("\n");
                // System.out.println(joinedLine);
            }
        }
        return output.toString();
        // System.out.println(output);
    }

    void printTable() {
        System.out.println(filterTableWithAttributesAndCondition(null, null));
    }

    boolean conditionIsMet(List<Token> condition, Row row) {
        if (condition == null) return true;
        int orPosition = evaluateBooleanSubExpression(condition, row, "OR");
        if (orPosition != -1) {
            List<Token> leftHandSide = condition.subList(0, orPosition);
            List<Token> rightHandSide = condition.subList(orPosition + 1, condition.size());
            return (conditionIsMet(leftHandSide, row) || conditionIsMet(rightHandSide, row));
        }
        int andPosition = evaluateBooleanSubExpression(condition, row, "AND");
        if (andPosition != -1) {
            List<Token> leftHandSide = condition.subList(0, andPosition);
            List<Token> rightHandSide = condition.subList(andPosition + 1, condition.size());
            return (conditionIsMet(leftHandSide, row) && conditionIsMet(rightHandSide, row));
        }
        if (condition.size() == 3 || condition.size() == 5) {
            return tertiaryCondition(condition, row);
        }
        return false;
    }

    int evaluateBooleanSubExpression(List<Token> condition, Row row, String operator) {
        // only if at top level
        // find boolean operator
        int depth = 0;
        boolean booleanOperatorFound = false;
        int position = 0;
        for (int i = 0; i < condition.size(); i++) {
            if (condition.get(i).toString().equals("(")) depth++;
            else if (condition.get(i).toString().equals(")")) depth--;
            else if (depth == 0 && !booleanOperatorFound && condition.get(i).getValue().equals(operator)) {
                booleanOperatorFound = true;
                position = i;
            }
        }
        // split conditional
        if (booleanOperatorFound) {
            return position;
        }
        return -1;
    }

    List<Token> evaluateParentheses(List<Token> condition, Row row) {
        List<Token> fragment = new ArrayList<>();
        int openParenthesesCount = 0;
        int closeParenthesesCount = 0;
        boolean openParentheses = false;
        for (int i = 0; i < condition.size(); i++) {
            if (condition.get(i).getValue().equals("(")) {
                openParenthesesCount++;
                openParentheses = true;
            }
            else if (openParentheses && condition.get(i).getValue().equals(")")) {
                closeParenthesesCount++;
            }
            if (openParenthesesCount == closeParenthesesCount) {
                fragment = condition.subList(1, i);
            }
        }
        // no parentheses
        return condition;
    }



        /*
        else {
            search left to right
            find boolean
            if and:
                return (conditionismet(left hand side) and conditionismet (right hand side))
            and the same for or:
                ...

        }
         */

    boolean tertiaryCondition(List<Token> condition, Row row) {
        int first, second, third;
        first = 0;
        second = 1;
        third = 2;
        if (condition.size() == 5) {
            first++;
            second++;
            third++;
        }
        String attributeToken = condition.get(first).getValue();
        String comparator = condition.get(second).getValue();
        Token valueToken = condition.get(third);
        Token valueInCell = row.attributesToValues.get(attributeToken);

        String dataType = null;
        for (Attribute attribute : attributes) {
            if (attributeToken.equals(attribute.getName())) {
                dataType = attributes.get(attribute.getIndex()).getDataType();
            }
        }

        switch (dataType) {
            case "booleanLiteral":
                return booleanCondition(valueToken, valueInCell, comparator);
            case "integerLiteral":
                return integerCondition(valueToken, valueInCell, comparator);
            case "floatLiteral":
                return floatCondition(valueToken, valueInCell, comparator);
            case "stringLiteral":
                return stringCondition(valueToken, valueInCell, comparator);
        }
        return false;
    }

    boolean booleanCondition(Token valueToken, Token valueInCell, String comparator) {
        boolean value = valueInCell.getBooleanValue();
        boolean query = valueToken.getBooleanValue();
        switch (comparator) {
            case "==":
                return (value == query);
            case "!=":
                return (value != query);
        }
        return false;
    }

    boolean integerCondition(Token valueToken, Token valueInCell, String comparator) {
        int value = valueInCell.getIntegerValue();
        int query = valueToken.getIntegerValue();
        switch (comparator) {
            case "==":
                return (value == query);
            case ">":
                return (value > query);
            case "<":
                return (value < query);
            case ">=":
                return (value >= query);
            case "<=":
                return (value <= query);
            case "!=":
                return (value != query);
        }
        return false;
    }

    boolean floatCondition(Token valueToken, Token valueInCell, String comparator) {
        float value = valueInCell.getFloatValue();
        float query = valueToken.getFloatValue();
        switch (comparator) {
            case "==":
                return (value == query);
            case ">":
                return (value > query);
            case "<":
                return (value < query);
            case ">=":
                return (value >= query);
            case "<=":
                return (value <= query);
            case "!=":
                return (value != query);
        }
        return false;
    }

    boolean stringCondition(Token valueToken, Token valueInCell, String comparator) {
        String value = valueInCell.getValue();
        String query = valueToken.getValue();
        switch (comparator) {
            case "==":
                return (value.equals(query));
            case ">":
                return (value.length() > query.length());
            case "<":
                return (value.length() < query.length());
            case ">=":
                return (value.length() >= query.length());
            case "<=":
                return (value.length() <= query.length());
            case "!=":
                return (!value.equals(query));
            case "LIKE":
                return (query.contains(value));
        }
        return false;
    }

    void deleteFromTableOnCondition(List<Token> condition) throws FileNotFoundException {
        int rowCount = rows.size();
        for (int i = 0; i < rowCount; i++) {
            if (conditionIsMet(condition, rows.get(i))) {
                rows.remove(i);
                rowCount--;
            }
        }
        updateTable();
    }

}
