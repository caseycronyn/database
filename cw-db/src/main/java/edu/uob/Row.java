package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {
    Map<String, Token> attributesToValues = new HashMap<>();
    List<Attribute> attributes;
    List<Token> tokenList;
    int id;

    Row (List<Attribute> attributes, List<Token> tokenList, int newID) {
        this.attributes = attributes;
        this.tokenList = tokenList;
        this.id = newID;
        createValuesAndInitialiseValueList();
        mapAttributesToValues();
    }

    Row copy() {
        Map<String, Token> newAttributesToValues = new HashMap<>();
        for (String attributeName : attributesToValues.keySet()) {
            newAttributesToValues.put(attributeName, attributesToValues.get(attributeName).copy());
        }

        List<Attribute> newAttributes = new ArrayList<>();
        for (Attribute attribute : attributes) {
            newAttributes.add(attribute.copy());
        }

        List<Token> newTokenList = new ArrayList<>();
        for (Token token : tokenList) {
            newTokenList.add(token.copy());
        }
        Row newRow = new Row(newAttributes, newTokenList, id);
        newRow.addAttributesToValuesMap(newAttributesToValues);

        return newRow;
    }

    void changeKeyInAttributesToValuesMap(String attributeName, String replacementName) {
        Token returnedToken = attributesToValues.remove(attributeName);
        attributesToValues.put(replacementName, returnedToken);
    }

    void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    void changeAttributeName(String attributeName, String newAttributeName) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(attributeName)) {
                attribute.setName(newAttributeName);
            }
        }
    }

    void addAttributes(List<Attribute> newAttributes) {
        attributes.addAll(newAttributes);
    }


    void addValueToRow(String attributeName, Token value) {
        attributesToValues.put(attributeName, value);
    }

    void clearAttributes() {
        attributesToValues.clear();
    }

    void clearTokenList() {
        tokenList.clear();
    }

    List<Attribute> getAttributes() {
        return attributes;
    }

    void addAttribute() {}

    void addNewTokenList (List<Token> newTokenListIn) {
        tokenList.addAll(newTokenListIn);
    }

    void addAttributesToValuesMap(Map<String, Token> attributesToValuesIn) {
        attributesToValues.putAll(attributesToValuesIn);
    }

    void addNewAttributesList(List<Attribute> newAttributesIn) {
        attributes.addAll(newAttributesIn);
    }

    void createValuesAndInitialiseValueList() {
        addIdToken();
        for (int i = 1; i < attributes.size(); i++) {
            Token token = tokenList.get(i - 1);
            attributesToValues.put(attributes.get(i).getName(), token);
        }
    }

    int getId() {
        return id;
    }

    List<Token> getTokenList() {
        return tokenList;
    }


    void addIdToken() {
        Token token = new Token(Integer.toString(id), 0);
        token.setTokenType("integerLiteral");
        attributesToValues.put("id", token);
    }

    void changeId(int newId) {
        id = newId;
        Token token = new Token(Integer.toString(newId), 0);
        attributesToValues.replace("id", token);
    }

    void changeValue(String attribute, Token token) {
        attributesToValues.put(attribute, token);
    }

    Token getValueFromAttribute(String attribute) {
        return attributesToValues.get(attribute);
    }

    Value createValue() {
        return null;

    }

    void init() {


    }

    void mapAttributesToValues() {

    }


}
