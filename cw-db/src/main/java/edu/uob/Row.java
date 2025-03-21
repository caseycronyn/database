package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {
    Map<String, Token> attributeValueMap = new HashMap<>();
    List<Token> tokenList;
    int id;
    Table table;

    Row(List<Token> tokenList, int newID, Table table) {
        this.table = table;
        this.tokenList = tokenList;
        this.id = newID;
        if (tokenList != null) {
            addID();
            for (int i = 1; i < table.getAttributeList().size(); i++) {
                Token token = tokenList.get(i - 1);
                attributeValueMap.put(table.getAttributeAtIndex(i).getName(), token);
            }
        }
    }

    Row copyRow(Table newTable) {
        Map<String, Token> newAttributesToValues = new HashMap<>();
        for (String attributeName : attributeValueMap.keySet()) {
            newAttributesToValues.put(attributeName, attributeValueMap.get(attributeName).copyToken());
        }

        List<Token> newTokenList = new ArrayList<>();
        for (Token token : tokenList) {
            newTokenList.add(token.copyToken());
        }
        Row newRow = new Row(newTokenList, id, newTable);
        newRow.appendToValueMap(newAttributesToValues);

        return newRow;
    }

    void initialiseRow(String[] tokenArray) {
        for (int i = 0; i < table.getAttributeList().size(); i++) {
            Token token = new Token(tokenArray[i], i);
            token.setValueType();
            attributeValueMap.put(table.getAttributeAtIndex(i).getName(), token);
        }
    }

    void setMapAttribute(String attributeName, String replacementName) {
        Token returnedToken = attributeValueMap.remove(attributeName);
        attributeValueMap.put(replacementName, returnedToken);
    }

    void addRowValue(String attributeName, Token value) {
        attributeValueMap.put(attributeName, value);
    }

    void addAttributeToMap(String attributeName, Token value) {
        attributeValueMap.put(attributeName, value);
    }

    void appendToValueMap(Map<String, Token> attributesToValuesIn) {
        attributeValueMap.putAll(attributesToValuesIn);
    }

    int getId() {
        return id;
    }

    void addID() {
        Token token = new Token(Integer.toString(id), 0);
        token.setTokenType("integerLiteral");
        attributeValueMap.put("id", token);
    }

    void changeId(int newId) {
        id = newId;
        Token token = new Token(Integer.toString(newId), 0);
        attributeValueMap.replace("id", token);
    }

    void setValue(String attribute, Token token) {
        attributeValueMap.put(attribute, token);
    }

    Token getAttributeValue(String attribute) {
        return attributeValueMap.get(attribute);
    }
}
