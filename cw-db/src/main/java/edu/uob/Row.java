package edu.uob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {
    Map<String, Value> attributesToValues = new HashMap<>();
    List<String> attributes;
    List<Token> tokenList;
    List<Value> valueList;
    int id;

    Row (List<String> attributes, List<Token> tokenList, int newID) {
        this.attributes = attributes;
        this.tokenList = tokenList;
        this.id = newID;
        createValuesAndInitialiseValueList();
        mapAttributesToValues();
    }

    void createValuesAndInitialiseValueList() {
        for (String attribute : attributes) {
            Value value = createValue();
        }
    }

    Value createValue() {
        return null;

    }

    void init() {


    }

    void mapAttributesToValues() {

    }


}
