package edu.uob;

public class Value {
    String dataType, stringValue;

    Value(Token token)
    {
        dataType = token.getTokenType();
        stringValue = token.getValue();
    }

    String getStringValue() {
        return stringValue;
    }

    String getDataType() {
        return dataType;
    }

    int getIntegerValue() {
        return Integer.parseInt(stringValue);
    }

    float getFloatValue() {
        return Float.parseFloat(stringValue);
    }

    boolean getBooleanValue() {
        return Boolean.parseBoolean(stringValue);
    }
}
