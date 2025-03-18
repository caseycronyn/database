package edu.uob;

public class Token {
    private String value, tokenType;
    private Integer position;

    Token (String name, Integer position) {
        this.value = name;
        this.position = position;
        tokenType = null;
        // dataType = null;
    }

    Token copy() {
        String newName = new String(value);
        String newTokenType = new String(tokenType);
        Token token = new Token(newName, position);
        token.setTokenType(newTokenType);
        return token;
    }

    void setDataType() {
        // this.dataType = dataType;
    }

    int returnInt() {
        return Integer.parseInt(value);
    }

    void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    String getTokenType() {
        return tokenType;
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    void nameToUpperCase() {
        value = value.toUpperCase();
    }

    int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    int getIntegerValue() {
        return Integer.parseInt(value);
    }

    float getFloatValue() {
        return Float.parseFloat(value);
    }

    boolean getBooleanValue() {
        return Boolean.parseBoolean(value);
    }

    boolean tokenIsParenthesis () {
        return value.equals("openParenthesis") || value.equals("closeParenthesis");
    }

}
