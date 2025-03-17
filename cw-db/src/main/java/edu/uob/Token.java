package edu.uob;

public class Token {
    private String name, tokenType;
    private Integer position;

    Token (String name, Integer position) {
        this.name = name;
        this.position = position;
        tokenType = null;
        // dataType = null;
    }

    Token copy() {
        String newName = new String(name);
        String newTokenType = new String(tokenType);
        Token token = new Token(newName, position);
        token.setTokenType(newTokenType);
        return token;
    }

    void setDataType() {
        // this.dataType = dataType;
    }

    int returnInt() {
        return Integer.parseInt(name);
    }

    void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    String getTokenType() {
        return tokenType;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    void nameToUpperCase() {
        name = name.toUpperCase();
    }

    int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    int getIntegerValue() {
        return Integer.parseInt(name);
    }

    float getFloatValue() {
        return Float.parseFloat(name);
    }

    boolean getBooleanValue() {
        return Boolean.parseBoolean(name);
    }

    boolean tokenIsParenthesis () {
        return name.equals("openParenthesis") || name.equals("closeParenthesis");
    }

}
