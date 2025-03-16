package edu.uob;

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

    void createValuesAndInitialiseValueList() {
        addId();
        for (int i = 1; i < attributes.size(); i++) {
            Token token = tokenList.get(i - 1);
            attributesToValues.put(attributes.get(i).getName(), token);
        }
    }

    void addId() {
        Token token = new Token(Integer.toString(id), -1);
        token.setTokenType("integerLiteral");
        attributesToValues.put(attributes.get(0).getName(), token);
    }

    Value createValue() {
        return null;

    }

    void init() {


    }

    void mapAttributesToValues() {

    }


}
