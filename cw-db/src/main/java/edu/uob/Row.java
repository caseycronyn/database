package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row extends TokenBank {
    Map<String, Token> attributesToTokens = new HashMap<>();
    List<Token> tokenList;
    int id;
    Table table;

    Row (List<Token> tokenList, int newID, Table table) {
        this.table = table;
        this.tokenList = tokenList;
        this.id = newID;
        if (tokenList != null) {
            addIdToken();
            for (int i = 1; i < table.getAttributes().size(); i++) {
                Token token = tokenList.get(i - 1);
                attributesToTokens.put(table.getAttributeAtIndex(i).getName(), token);
            }
        }
    }

    Row copy(Table newTable) {
        Map<String, Token> newAttributesToValues = new HashMap<>();
        for (String attributeName : attributesToTokens.keySet()) {
            newAttributesToValues.put(attributeName, attributesToTokens.get(attributeName).copy());
        }

        List<Token> newTokenList = new ArrayList<>();
        for (Token token : tokenList) {
            newTokenList.add(token.copy());
        }
        Row newRow = new Row(newTokenList, id, newTable);
        newRow.addAttributesToValuesMap(newAttributesToValues);

        return newRow;
    }


    void changeKeyInAttributesToValuesMap(String attributeName, String replacementName) {
        Token returnedToken = attributesToTokens.remove(attributeName);
        attributesToTokens.put(replacementName, returnedToken);
    }

    void addValueToRow(String attributeName, Token value) {
        attributesToTokens.put(attributeName, value);
    }

    void addAttributeToValueMap(String attributeName, Token value) {
        attributesToTokens.put(attributeName, value);
    }

    void addAttributesToValuesMap(Map<String, Token> attributesToValuesIn) {
        attributesToTokens.putAll(attributesToValuesIn);
    }

    void initialiseRow(String[] tokenArray) {
        for (int i = 0; i < table.getAttributes().size(); i++) {
            Token token = new Token(tokenArray[i], i);
            token.setValueTokenType();
            attributesToTokens.put(table.getAttributeAtIndex(i).getName(), token);
        }
    }

    int getId() {
        return id;
    }

    void addIdToken() {
        Token token = new Token(Integer.toString(id), 0);
        token.setTokenType("integerLiteral");
        attributesToTokens.put("id", token);
    }

    void changeId(int newId) {
        id = newId;
        Token token = new Token(Integer.toString(newId), 0);
        attributesToTokens.replace("id", token);
    }

    void changeValue(String attribute, Token token) {
        attributesToTokens.put(attribute, token);
    }

    Token getValueFromAttribute(String attribute) {
        return attributesToTokens.get(attribute);
    }
}
