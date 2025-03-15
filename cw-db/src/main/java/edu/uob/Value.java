package edu.uob;

public class Value {
    int integerValue = -1;
    float floatValue = -1;
    String stringValue = null;
    boolean booleanValue;
    String type, name;


    Value(Token token)
    {
        type = token.getTokenType();
        name = token.getName();
    }

    String getName() {
        return name;
    }

    String getType() {
        return type;
    }

    int getIntegerValue() {
        return integerValue;
    }

    float getFloatValue() {
        return floatValue;
    }

    String getStringValue() {
        return stringValue;
    }

    boolean getBooleanValue() {
        return booleanValue;
    }

    void setIntegerValue() {
        ;
    }

}
