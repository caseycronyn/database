package edu.uob;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Table {
    List<Attribute> attributes = new ArrayList<>();
    List<Row> rows = new ArrayList<>();
    String tableName, databaseName, storageFolderPath;

    public Table(String tableName, String databaseName, String storageFolderPath) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.storageFolderPath = storageFolderPath;
    }

    Table copyTable() {
        List<Attribute> newAttributes = new ArrayList<>();
        for (Attribute attribute : attributes) {
            newAttributes.add(attribute.copyAttribute());
        }

        Table newTable = new Table(tableName, databaseName, storageFolderPath);
        List<Row> newRows = new ArrayList<>();
        for (Row row : rows) {
            newRows.add(row.copyRow(newTable));
        }
        newTable.appendRows(newRows);
        newTable.appendAttributes(newAttributes);

        return newTable;
    }

    void initialiseTable(Database database) throws IOException {
        ArrayList<String> buffer = new ArrayList<>();
        fillStringBuffer(buffer);
        readInAttributes(buffer);
        for (int i = 1; i < buffer.size(); i++) {
            String[] tabArray = buffer.get(i).split("\t");
            Row row = new Row(null, database.getNewID(), this);
            row.initialiseRow(tabArray);
            rows.add(row);
        }
    }

    void appendRows(List<Row> rowsIn) {
        rows.addAll(rowsIn);
    }

    void appendAttributes(List<Attribute> attributesIn) {
        attributes.addAll(attributesIn);
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

    List<Attribute> getAttributeList() {
        return attributes;
    }

    void updateTable() throws FileNotFoundException {
        writeToFile();
        updateAttributeTypes();
    }

    void updateAttributeTypes() {
        Map<String, String> valueSet = new HashMap<>();
        String dataType = null;
        for (int i = 1; i < attributes.size(); i++) {
            valueSet.clear();
            for (Row row : rows) {
                dataType = row.attributeValueMap.get(attributes.get(i).getName()).getTokenType();
                valueSet.put(dataType, dataType);
            }
            if (valueSet.size() > 1) {
                dataType = fixTypeConflict(valueSet);
            }
            attributes.get(i).setDataType(dataType);
        }
    }

    String fixTypeConflict(Map<String, String> valueSet) {
        if (valueSet.containsKey("floatLiteral") && valueSet.containsKey("stringLiteral")) {
            return "floatLiteral";
        } else return "stringLiteral";
    }

    void updateFilteredRows(List<Token> nameValuePairs, List<Token> conditions) throws FileNotFoundException {
        for (Row row : rows) {
            if (conditionIsMet(conditions, row)) {
                updateRowTypes(nameValuePairs, row);
            }
        }
        updateTable();
    }

    void updateRowTypes(List<Token> nameValuePairs, Row row) {
        for (int i = 0; i < nameValuePairs.size(); i += 3) {
            Token nameToken = nameValuePairs.get(i);
            Token typeToken = nameValuePairs.get(i + 2);
            row.setValue(nameToken.getValue(), typeToken);
        }
    }

    void addAttributeList(List<Token> tokens) throws FileNotFoundException {
        int i = 0;
        Attribute ID = new Attribute("id", "integerLiteral", i);
        attributes.add(ID);
        for (Token token : tokens) {
            Attribute newAttribute = new Attribute(token.getValue(), "NULL", ++i);
            attributes.add(newAttribute);
        }
        updateTable();
    }

    public void readInAttributes(ArrayList<String> stringBuffer) {
        if (!stringBuffer.isEmpty()) {
            String command = stringBuffer.get(0);
            List<String> attributeList = new ArrayList<>(List.of(command.split("\t")));
            int i = 0;
            for (String attribute : attributeList) {
                Attribute newAttribute = new Attribute(attribute, "NULL", i++);
                attributes.add(newAttribute);
            }
        }
    }

    public void fillStringBuffer(ArrayList<String> stringBuffer) throws IOException {
        BufferedReader bufferReader;
        FileReader fileReader;
        File newFile = new File(storageFolderPath + File.separator + databaseName + File.separator + tableName + ".tab");
        fileReader = new FileReader(newFile);
        bufferReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferReader.readLine()) != null) {
            stringBuffer.add(line);
        }
        bufferReader.close();
    }

    public void writeToFile() throws FileNotFoundException {
        String newFileName = storageFolderPath + File.separator + databaseName + File.separator + tableName + ".tab";
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(newFileName);
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException("error writing to file " + newFileName);
        }
        StringBuilder firstLine = new StringBuilder();
        for (Attribute attribute : attributes) {
            firstLine.append(attribute.getName()).append("\t");
        }
        printWriter.println(firstLine);
        ArrayList<String> orderedValues = new ArrayList<>();
        // for each row of entries: make ordered array
        for (Row row : rows) {
            orderedValues.clear();
            // loop through attributes in order
            for (Attribute attribute : attributes) {
                orderedValues.add(row.attributeValueMap.get(attribute.getName()).getValue());
            }
            String joinedLine = String.join("\t", orderedValues);
            printWriter.println(joinedLine);
        }
        printWriter.close();
    }

    public void writeEmptyFile() throws IOException {
        try {
            Files.createFile(Paths.get(storageFolderPath + File.separator + databaseName + File.separator + tableName + ".tab"));
        }
        catch (FileAlreadyExistsException ignore){}
    }

    public void addAttribute(String name) throws FileNotFoundException {
        Attribute newAttribute = new Attribute(name, "NULL", attributes.size());
        attributes.add(newAttribute);
        for (Row row : rows) {
            Token newToken = new Token(null, attributes.size());
            row.addAttributeToMap(name, newToken);
        }
        updateTable();
    }


    public void removeAttribute(String name) {
        attributes.removeIf(attribute -> attribute.getName().equals(name));
    }

    public void addRow(List<Token> valueList, Integer newID) throws FileNotFoundException {
        Row newRow = new Row(valueList, newID, this);
        rows.add(newRow);
        updateTable();
    }

    Row getRowAtIndex(int index) {
        return rows.get(index);
    }

    Row getRowAtID(int ID) {
        for (Row row : rows) {
            if (row.getAttributeValue("id").getIntegerValue() == ID) {
                return row;
            }
        }
        return null;
    }

    public String filterTable(List<Token> selectedAttributes, List<Token> condition) {
        // select all if null
        if (selectedAttributes == null) {
            selectedAttributes = getAttributeTokens();
        }
        // attributes
        StringBuilder outputString = new StringBuilder();
        StringBuilder firstLine = new StringBuilder();
        for (int i = 0; i < selectedAttributes.size(); i++) {
            firstLine.append(selectedAttributes.get(i).getValue());
            if (i != selectedAttributes.size() - 1) {
                firstLine.append("\t");
            }
        }
        outputString.append(firstLine.append("\n"));
        return filterRows(selectedAttributes, condition, outputString);
    }

    String filterRows(List<Token> selectedAttributes, List<Token> condition, StringBuilder output) {
        for (Row row : rows) {
            if (conditionIsMet(condition, row)) {
                ArrayList<String> orderedValues = new ArrayList<>();
                for (Token attribute : selectedAttributes) {
                    String attributeValue = row.attributeValueMap.get(attribute.getValue()).getValue();
                    if (attributeValue != null) {
                        orderedValues.add(attributeValue);
                    }
                }
                String joinedLine = String.join("\t", orderedValues);
                output.append(joinedLine).append("\n");
            }
        }
        return output.toString();
    }

    List<Token> getAttributeTokens() {
        List<Token> attributeTokens = new ArrayList<>();
        for (Attribute attribute : attributes) {
            Token newToken = new Token(attribute.getName(), -1);
            attributeTokens.add(newToken);
        }
        return attributeTokens;
    }


    boolean attributesExist(List<Token> attributeList) {
        for (Token attribute : attributeList) {
            if (attributeExists(attribute.getValue())) {
                return true;
            }
        }
        return false;
    }

    boolean attributeExists(String attributeName) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(attributeName)) return true;
        }
        return false;
    }

    boolean conditionIsMet(List<Token> condition, Row row) {
        if (condition == null) return true;
        int orPosition = resolveSegment(condition, "OR");
        if (orPosition != -1) {
            List<Token> leftHandSide = condition.subList(0, orPosition);
            List<Token> rightHandSide = condition.subList(orPosition + 1, condition.size());
            return (conditionIsMet(leftHandSide, row) || conditionIsMet(rightHandSide, row));
        }
        int andPosition = resolveSegment(condition, "AND");
        if (andPosition != -1) {
            List<Token> leftHandSide = condition.subList(0, andPosition);
            List<Token> rightHandSide = condition.subList(andPosition + 1, condition.size());
            return (conditionIsMet(leftHandSide, row) && conditionIsMet(rightHandSide, row));
        }
        if (condition.size() == 3 || condition.size() == 5) {
            return attributeSegment(condition, row);
        }
        return false;
    }



    int resolveSegment(List<Token> condition, String operator) {
        //  find boolean operator if at top level
        int parenthesesDepth = 0;
        boolean booleanFound = false;
        int booleanPosition = 0;
        for (int i = 0; i < condition.size(); i++) {
            if (condition.get(i).toString().equals("(")) parenthesesDepth++;
            else if (condition.get(i).toString().equals(")")) parenthesesDepth--;
            else if (parenthesesDepth == 0 && !booleanFound && condition.get(i).getValue().equals(operator)) {
                booleanFound = true;
                booleanPosition = i;
            }
        }
        // split conditional
        if (booleanFound) return booleanPosition;
        return -1;
    }

    boolean attributeSegment(List<Token> condition, Row row) {
        int tokenOne = 0;
        int tokenTwo = 1;
        int tokenThree = 2;
        if (condition.size() == 5) {
            tokenOne++;
            tokenTwo++;
            tokenThree++;
        }
        String attributeToken = condition.get(tokenOne).getValue();
        String comparator = condition.get(tokenTwo).getValue();
        Token valueToken = condition.get(tokenThree);
        Token attributeValue = row.attributeValueMap.get(attributeToken);

        return dataTypeSwitch(attributeToken, comparator, valueToken, attributeValue);
    }

    boolean dataTypeSwitch(String attributeToken, String comparator, Token valueToken, Token valueInCell) {
        String dataType = null;
        for (Attribute attribute : attributes) {
            if (attributeToken.equals(attribute.getName())) {
                dataType = attributes.get(attribute.getIndex()).getDataType();
            }
        }
        if (dataType != null) {
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
                return (value.contains(query));
        }
        return false;
    }

    void deleteOnFilter(List<Token> condition) throws FileNotFoundException {
        int rowCount = rows.size();
        for (int i = 0; i < rowCount; i++) {
            if (conditionIsMet(condition, rows.get(i))) {
                rows.remove(i);
                i--;
                rowCount--;
            }
        }
        updateTable();
    }
}
